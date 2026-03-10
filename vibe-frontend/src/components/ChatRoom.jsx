import { useState, useEffect } from 'react';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

export default function ChatRoom() {
  const [messages, setMessages] = useState([]);

  useEffect(() => {
    const socket = new SockJS('http://localhost:8080/vibe-ws');
    const client = Stomp.over(socket);

    client.connect({}, () => {
      client.subscribe('/topic/messages', (msg) => {
        const newMessage = JSON.parse(msg.body);
        setMessages((prev) => [...prev, newMessage]);
      });
    });

    return () => client.disconnect();
  }, []);

  return (
    <div>
      {messages.map((m, i) => <p key={i}>{m.sender}: {m.content}</p>)}
    </div>
  );
}