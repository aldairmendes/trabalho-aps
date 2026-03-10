import React from 'react';

export default function Contact({ name, phone, avatar, onClick }) {
  return (
    <div className="contact-item" onClick={onClick}>
      <div className="contact-avatar">{avatar}</div>
      <div className="contact-info">
        <h3>{name}</h3>
        <p>{phone}</p>
      </div>
    </div>
  );
}