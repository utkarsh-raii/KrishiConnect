import { useState, useEffect, useRef } from 'react';
import { Modal } from './UI';
import { useAuth } from '../../context/AuthContext';
import api from '../../api/axios';
import { createStompClient } from '../../services/websocket';
import toast from 'react-hot-toast';
import { HiOutlinePaperAirplane, HiOutlineChatAlt2 } from 'react-icons/hi';

export default function ChatModal({ open, onClose, otherUserId, cropId, cropName, otherUserName }) {
  const { token, user } = useAuth();
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState('');
  const [loading, setLoading] = useState(false);
  const clientRef = useRef(null);
  const scrollRef = useRef();

  const currentUserId = user?._id || user?.id;

  // STOMP WebSocket setup
  useEffect(() => {
    if (!token || !open || !currentUserId) return;

    const client = createStompClient({
      token,
      onConnect: (stompClient) => {
        stompClient.subscribe(`/topic/messages/${currentUserId}`, (stompMsg) => {
          try {
            const data = JSON.parse(stompMsg.body);
            if (!cropId || data.cropId === cropId) {
              setMessages((prev) => {
                const exists = prev.some((m) => (m._id && m._id === data._id) || (m.id && m.id === data.id));
                return exists ? prev : [...prev, data];
              });
            }
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
      console.warn('STOMP activation warning:', e);
    }

    return () => {
      if (clientRef.current) {
        clientRef.current.deactivate();
        clientRef.current = null;
      }
    };
  }, [token, open, currentUserId, cropId, otherUserId]);

  // Fetch history
  const fetchMessages = async () => {
    try {
      setLoading(true);
      const res = await api.get('/messages', {
        params: {
          otherUserId,
          cropId: cropId || undefined,
        },
      });
      if (res.data && res.data.success) {
        setMessages(res.data.messages || res.data.data || []);
      }
    } catch (error) {
      toast.error('Failed to load chat history');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (open && otherUserId) {
      fetchMessages();
    }
  }, [open, otherUserId, cropId]);

  // Scroll to bottom
  useEffect(() => {
    scrollRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages]);

  const handleSend = async (e) => {
    e.preventDefault();
    if (!newMessage.trim()) return;

    const tempMessage = {
      receiverId: otherUserId,
      cropId,
      message: newMessage.trim(),
      senderId: currentUserId,
      senderRole: user?.role,
      createdAt: new Date().toISOString(),
    };

    try {
      setMessages((prev) => [...prev, tempMessage]);
      setNewMessage('');

      const res = await api.post('/messages', {
        receiverId: otherUserId,
        cropId: cropId || undefined,
        message: tempMessage.message,
      });

      if (res.data && res.data.data) {
        // Update local message with server response if needed
        setMessages((prev) =>
          prev.map((m) => (m === tempMessage ? res.data.data : m))
        );
      }
    } catch (error) {
      toast.error('Failed to send message');
    }
  };

  return (
    <Modal
      open={open}
      onClose={onClose}
      title={`Chat with ${otherUserName || 'User'}`}
      width="max-w-md"
    >
      <div className="flex flex-col h-[500px]">
        {/* Header Info */}
        {cropName && (
          <div className="px-4 py-2 bg-forest-50 border-b border-forest-100 flex items-center gap-2 mb-4 rounded-xl">
            <HiOutlineChatAlt2 className="text-forest-600" />
            <span className="text-xs font-bold text-forest-800 uppercase tracking-wider">
              Topic: {cropName}
            </span>
          </div>
        )}

        {/* Messages List */}
        <div className="flex-1 overflow-y-auto space-y-4 pr-2 custom-scrollbar pb-4">
          {loading ? (
            <div className="flex justify-center py-10">
              <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-forest-600"></div>
            </div>
          ) : messages.length === 0 ? (
            <div className="text-center py-20 text-gray-400 italic text-sm">
              No messages yet. Start the conversation!
            </div>
          ) : (
            messages.map((msg, index) => {
              const isMine = msg.senderId === currentUserId;
              return (
                <div
                  key={index}
                  className={`flex ${isMine ? 'justify-end' : 'justify-start'}`}
                >
                  <div
                    className={`max-w-[80%] px-4 py-2 rounded-2xl text-sm ${
                      isMine
                        ? 'bg-forest-600 text-white rounded-tr-none shadow-lg shadow-forest-100'
                        : 'bg-gray-100 text-gray-800 rounded-tl-none border border-gray-200'
                    }`}
                  >
                    <p>{msg.message}</p>
                    <p
                      className={`text-[10px] mt-1 opacity-60 ${
                        isMine ? 'text-right' : 'text-left'
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

        {/* Input Area */}
        <form onSubmit={handleSend} className="mt-4 flex gap-2">
          <input
            type="text"
            className="form-input flex-1 bg-gray-50 border-gray-200 focus:bg-white transition-all rounded-xl py-3"
            placeholder="Type your message..."
            value={newMessage}
            onChange={(e) => setNewMessage(e.target.value)}
          />
          <button
            type="submit"
            className="w-12 h-12 rounded-xl bg-forest-600 text-white flex items-center justify-center hover:bg-forest-700 transition-colors shadow-lg shadow-forest-100 disabled:opacity-50"
            disabled={!newMessage.trim()}
          >
            <HiOutlinePaperAirplane className="w-6 h-6 rotate-45" />
          </button>
        </form>
      </div>
    </Modal>
  );
}
