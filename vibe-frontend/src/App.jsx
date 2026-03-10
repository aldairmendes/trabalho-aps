import React, { useState } from 'react';
import Sidebar from './components/Sidebar';
import Contact from './components/Contact'; // O molde que você criou
import ChatRoom from './components/ChatRoom'; // Onde está o WebSocket
import './App.css';

function App() {
  // Estado para saber qual chat está aberto (null = lista de contatos)
  const [selectedContact, setSelectedContact] = useState(null);

  // Lista de contatos (Mock) baseada no seu desenho
  const contacts = [
    { id: 1, name: "Contact 1", phone: "+55 (12) 34567-890", avatar: "👤" },
    { id: 2, name: "Mendes15", phone: "12345678", avatar: "👤" }
  ];

  return (
    <div className="main-layout" style={{ display: 'flex' }}>
      <Sidebar /> {/* Sua barra lateral fixa */}

      <main style={{ flex: 1, backgroundColor: '#fff' }}>
        {selectedContact ? (
          /* Se houver um contato selecionado, mostra o Chat */
          <div className="chat-container">
            <button onClick={() => setSelectedContact(null)}>⬅ Voltar</button>
            <ChatRoom contactName={selectedContact.name} />
          </div>
        ) : (
          /* Se não, mostra a lista conforme o seu caderno */
          <div className="list-container">
            <header className="list-header">
              <h1>LOGO</h1>
              <span>🔍</span>
            </header>
            
            {contacts.map((c) => (
              <Contact 
                key={c.id} 
                {...c} 
                onClick={() => setSelectedContact(c)} 
              />
            ))}
          </div>
        )}
      </main>
    </div>
  );
}

export default App;