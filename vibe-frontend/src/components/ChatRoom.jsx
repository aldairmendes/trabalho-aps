import { useState, useEffect, useRef } from 'react';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

export default function ChatRoom() {
  const [messages, setMessages] = useState([]);
  const [inputValue, setInputValue] = useState('');
  const stompClient = useRef(null);

  useEffect(() => {
    const socket = new SockJS('http://localhost:8080/vibe-ws');
    stompClient.current = Stomp.over(socket);

    stompClient.current.connect({}, () => {
      stompClient.current.subscribe('/topic/messages', (msg) => {
        const newMessage = JSON.parse(msg.body);
        setMessages((prev) => [...prev, newMessage]);
      });
    });

    return () => {
      if (stompClient.current) stompClient.current.disconnect();
    };
  }, []);

  const sendMessage = (e) => {
    e.preventDefault();
    if (stompClient.current && stompClient.current.connected && inputValue) {
      stompClient.current.send("/app/chat", {}, JSON.stringify({
        sender: "Mendes15",
        content: inputValue
      }));
      setInputValue(''); // Limpa o campo após enviar
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