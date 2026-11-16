import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

export const createStompClient = ({ token, onConnect, onError }) => {
  const wsUrl = import.meta.env.VITE_WS_URL || 'http://localhost:5001/ws';

  const client = new Client({
    webSocketFactory: () => new SockJS(wsUrl),
    connectHeaders: token ? { Authorization: `Bearer ${token}` } : {},
    debug: () => {},
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
    onConnect: (frame) => {
      if (onConnect) onConnect(client, frame);
    },
    onStompError: (frame) => {
      console.error('STOMP broker error:', frame);
      if (onError) onError(frame);
    },
    onWebSocketClose: () => {
      // WebSocket closed
    },
  });

  return client;
};
