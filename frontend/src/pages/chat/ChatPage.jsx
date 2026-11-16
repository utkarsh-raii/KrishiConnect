import { useEffect, useState, useRef } from 'react';
import { useSearchParams } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import api from '../../api/axios';
import { createStompClient } from '../../services/websocket';
import toast from 'react-hot-toast';
import { HiOutlinePaperAirplane, HiOutlineChatAlt2, HiOutlineUser } from 'react-icons/hi';

export default function ChatPage() {
  const { user, token } = useAuth();
  const [searchParams] = useSearchParams();
  const [messages, setMessages] = useState([]);
  const [text, setText] = useState('');
  const [loading, setLoading] = useState(false);
  const clientRef = useRef(null);
  const scrollRef = useRef(null);

  const currentUserId = user?._id || user?.id;
  const initialRecipientId = searchParams.get('recipientId') || searchParams.get('userId') || '1';
  const [recipientId, setRecipientId] = useState(initialRecipientId);
  const cropId = searchParams.get('cropId');

  // STOMP WebSocket setup
  useEffect(() => {
    if (!token || !currentUserId) return;

    const client = createStompClient({
      token,
      onConnect: (stompClient) => {
        stompClient.subscribe(`/topic/messages/${currentUserId}`, (stompMsg) => {
          try {
            const data = JSON.parse(stompMsg.body);
            setMessages((prev) => {
              const exists = prev.some((m) => (m._id && m._id === data._id) || (m.id && m.id === data.id));
              return exists ? prev : [...prev, data];
            });
          } catch (err) {
            console.error('Failed to parse STOMP message:', err);
          }
        });
      },
    });

    try {
      client.activate();
      clientRef.current = client;
    } catch (e) {
      console.warn('STOMP activation error:', e);
    }

    return () => {
      if (clientRef.current) {
        clientRef.current.deactivate();
        clientRef.current = null;
      }
    };
  }, [token, currentUserId]);

  const fetchMessages = async () => {
    if (!recipientId || !currentUserId) return;
    try {
      setLoading(true);
      const res = await api.get('/messages', {
        params: {
          otherUserId: recipientId,
          cropId: cropId || undefined,
        },
      });

      if (res.data && res.data.success) {
        setMessages(res.data.messages || res.data.data || []);
      }
    } catch (error) {
      console.error('Failed to fetch messages:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchMessages();
  }, [recipientId, cropId, currentUserId]);

  useEffect(() => {
    scrollRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages]);

  const sendMessage = async (e) => {
    if (e) e.preventDefault();
    if (!text.trim() || !recipientId) return;

    const optimisticMsg = {
      senderId: currentUserId,
      receiverId: Number(recipientId),
      senderRole: user?.role,
      cropId: cropId ? Number(cropId) : undefined,
      message: text.trim(),
      createdAt: new Date().toISOString(),
    };

    setMessages((prev) => [...prev, optimisticMsg]);
    const messageContent = text.trim();
    setText('');

    try {
      const res = await api.post('/messages', {
        receiverId: Number(recipientId),
        cropId: cropId ? Number(cropId) : undefined,
        message: messageContent,
      });

      if (res.data && res.data.data) {
        setMessages((prev) =>
          prev.map((m) => (m === optimisticMsg ? res.data.data : m))
        );
      }
    } catch (error) {
      toast.error('Failed to send message');
    }
  };

  return (
    <div className="max-w-4xl mx-auto p-4 sm:p-6 space-y-4">
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 bg-white p-4 rounded-2xl shadow-sm border border-gray-100">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-forest-50 text-forest-600 flex items-center justify-center">
            <HiOutlineChatAlt2 className="w-6 h-6" />
          </div>
          <div>
            <h1 className="text-xl font-bold text-gray-900">Live Messaging</h1>
            <p className="text-xs text-gray-500">Connected via Spring WebSocket & STOMP</p>
          </div>
        </div>

        <div className="flex items-center gap-2 w-full sm:w-auto">
          <label className="text-xs font-semibold text-gray-600 whitespace-nowrap">Recipient ID:</label>
          <input
            type="number"
            className="form-input py-1.5 px-3 text-sm w-24 rounded-lg"
            value={recipientId}
            onChange={(e) => setRecipientId(e.target.value)}
            placeholder="User ID"
          />
        </div>
      </div>

      <div className="bg-white rounded-2xl shadow-sm border border-gray-100 flex flex-col h-[520px]">
        <div className="flex-1 overflow-y-auto p-4 space-y-3 custom-scrollbar">
          {loading ? (
            <div className="flex justify-center py-20">
              <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-forest-600"></div>
            </div>
          ) : messages.length === 0 ? (
            <div className="text-center py-28 text-gray-400 italic text-sm">
              No messages found. Send a message to start chatting!
            </div>
          ) : (
            messages.map((msg, i) => {
              const isMine = String(msg.senderId) === String(currentUserId);
              return (
                <div
                  key={i}
                  className={`flex ${isMine ? 'justify-end' : 'justify-start'}`}
                >
                  <div
                    className={`max-w-[75%] px-4 py-2.5 rounded-2xl text-sm ${
                      isMine
                        ? 'bg-forest-600 text-white rounded-tr-none shadow-md shadow-forest-100'
                        : 'bg-gray-100 text-gray-800 rounded-tl-none border border-gray-200'
                    }`}
                  >
                    <p>{msg.message}</p>
                    <p
                      className={`text-[10px] mt-1 ${
                        isMine ? 'text-forest-100 text-right' : 'text-gray-400 text-left'
                      }`}
                    >
                      {new Date(msg.createdAt).toLocaleTimeString([], {
                        hour: '2-digit',
                        minute: '2-digit',
                      })}
                    </p>
                  </div>
                </div>
              );
            })
          )}
          <div ref={scrollRef} />
        </div>

        <form onSubmit={sendMessage} className="p-4 border-t border-gray-100 flex gap-2">
          <input
            value={text}
            onChange={(e) => setText(e.target.value)}
            placeholder="Type your message..."
            className="form-input flex-1 py-3 px-4 rounded-xl text-sm"
          />
          <button
            type="submit"
            className="btn-primary px-6 flex items-center justify-center gap-2"
            disabled={!text.trim()}
          >
            <span>Send</span>
            <HiOutlinePaperAirplane className="rotate-45" />
          </button>
        </form>
      </div>
    </div>
  );
}
