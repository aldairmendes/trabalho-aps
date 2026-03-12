import { useState, useEffect, useRef } from 'react';
// Removemos o SockJS e o import antigo do Stomp
import { Client } from '@stomp/stompjs';

export default function ChatRoom() {
  const [messages, setMessages] = useState([]);
  const [inputValue, setInputValue] = useState('');
  const stompClient = useRef(null);

  useEffect(() => {
    const client = new Client({
      brokerURL: 'ws://localhost:8080/vibe-ws',
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,

      onConnect: () => {
        console.log('Conectado ao WebSocket com sucesso!');
        
        client.subscribe('/topic/messages', (msg) => {
          if (msg.body) {
            const newMessage = JSON.parse(msg.body);
            setMessages((prev) => [...prev, newMessage]);
          }
        });
      },

      onStompError: (frame) => {
        console.error('Erro no Broker: ' + frame.headers['message']);
        console.error('Detalhes: ' + frame.body);
      },
    });

    client.activate();
    stompClient.current = client;

    return () => {
      if (stompClient.current) {
        stompClient.current.deactivate();
      }
    };
  }, []);

  const sendMessage = (e) => {
    e.preventDefault();
    
    if (stompClient.current && stompClient.current.active && inputValue) {
      stompClient.current.publish({
        destination: "/app/chat",
        body: JSON.stringify({
          sender: "Mendes15",
          content: inputValue
        })
      });
      setInputValue(''); 
    } else {
      console.warn("Conexão não está ativa.");
    }
  };

  return (
    <div className="chat-wrapper">
      <div className="messages-list">
        {messages.map((m, i) => (
          <div key={i} className={`message-wrapper ${m.sender === 'Mendes15' ? 'me' : 'others'}`}>
            <div className="message-bubble">
              <span className="sender-name">{m.sender}</span>
              <p>{m.content}</p>
            </div>
          </div>
        ))}
      </div>

      <form className="message-input-area" onSubmit={sendMessage}>
        <input 
          value={inputValue}
          onChange={(e) => setInputValue(e.target.value)}
          placeholder="Digite uma mensagem..."
        />
        <button type="submit">Enviar</button>
      </form>
    </div>
  );
}