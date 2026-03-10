import React from 'react';
import './Sidebar.css';

export default function Sidebar() {
  return (
    <div className="sidebar">
      <div className="sidebar-icon" title="Comunidades">🌐</div>
      <div className="sidebar-icon" title="Câmera">📷</div>
      <div className="sidebar-icon" title="Grupos">👥</div>
      
      <div style={{ marginTop: 'auto', marginBottom: '20px' }} className="sidebar-icon">
        ⚙️
      </div>
    </div>
  );
}