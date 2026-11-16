import { useState, useEffect, useRef } from 'react';
import { PageHeader, Button } from '../../components/common/UI';
import { useAuth } from '../../context/AuthContext';
import api from '../../api/axios';
import { createStompClient } from '../../services/websocket';
import toast from 'react-hot-toast';
import { HiOutlineChatAlt2, HiOutlinePaperAirplane } from 'react-icons/hi';

export default function BuyerChat() {
  const { user, token } = useAuth();
  const [messages, setMessages] = useState([]);
  const [message, setMessage] = useState('');
  const [recipientId, setRecipientId] = useState('1');
  const [loading, setLoading] = useState(false);
  const clientRef = useRef(null);
  const scrollRef = useRef(null);

  const currentUserId = user?._id || user?.id;

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
        params: { otherUserId: recipientId },
      });
      if (res.data && res.data.success) {
        setMessages(res.data.messages || res.data.data || []);
      }
    } catch (error) {
      console.error('Failed to load chat history:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchMessages();
  }, [recipientId, currentUserId]);

  useEffect(() => {
    scrollRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages]);

  const sendMessage = async (e) => {
    if (e) e.preventDefault();
    if (!message.trim() || !recipientId) {
      toast.error('Enter a message');
      return;
    }

    const optimisticMsg = {
      senderId: currentUserId,
      receiverId: Number(recipientId),
      senderRole: user?.role,
      message: message.trim(),
      createdAt: new Date().toISOString(),
    };

    setMessages((prev) => [...prev, optimisticMsg]);
    const messageContent = message.trim();
    setMessage('');

    try {
      const res = await api.post('/messages', {
        receiverId: Number(recipientId),
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
    <div className="space-y-6 max-w-4xl mx-auto">
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <PageHeader
          title="Direct Chat"
          subtitle="Communicate directly with farmers and service providers in real time."
        />

        <div className="flex items-center gap-2">
          <label className="text-xs font-semibold text-gray-600 whitespace-nowrap">Chat Partner ID:</label>
          <input
            type="number"
            className="form-input py-1.5 px-3 text-sm w-24 rounded-lg"
            value={recipientId}
            onChange={(e) => setRecipientId(e.target.value)}
            placeholder="User ID"
          />
        </div>
      </div>

      <div className="card h-[480px] flex flex-col p-4">
        <div className="flex-1 overflow-y-auto space-y-3 custom-scrollbar pr-2">
          {loading ? (
            <div className="flex justify-center py-20">
              <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-forest-600"></div>
            </div>
          ) : messages.length === 0 ? (
            <div className="text-center py-24 text-gray-400 italic text-sm">
              No messages found. Start a conversation!
            </div>
          ) : (
            messages.map((chat, idx) => {
              const isMine = String(chat.senderId) === String(currentUserId);
              return (
                <div
                  key={idx}
                  className={`flex ${isMine ? 'justify-end' : 'justify-start'}`}
                >
                  <div
                    className={`max-w-[75%] px-4 py-2.5 rounded-2xl text-sm ${
                      isMine
                        ? 'bg-forest-600 text-white rounded-tr-none shadow-sm'
                        : 'bg-gray-100 text-gray-800 rounded-tl-none border border-gray-200'
                    }`}
                  >
                    <p>{chat.message}</p>
                    <p
                      className={`text-[10px] mt-1 ${
                        isMine ? 'text-forest-100 text-right' : 'text-gray-400 text-left'
                      }`}
                    >
                      {new Date(chat.createdAt).toLocaleTimeString([], {
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

        <form onSubmit={sendMessage} className="pt-4 border-t border-gray-100 flex gap-2">
          <input
            className="form-input flex-1 py-2.5 px-4 rounded-xl text-sm"
            placeholder="Type your message..."
            value={message}
            onChange={(e) => setMessage(e.target.value)}
          />

          <Button type="submit" icon={HiOutlinePaperAirplane}>
            Send
          </Button>
        </form>
      </div>
    </div>
  );
}
