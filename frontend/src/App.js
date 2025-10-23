import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css';
import Header from './components/Header';
import Sidebar from './components/Sidebar';
import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import Profile from './pages/Profile';
import Timeline from './pages/Timeline';
import Friends from './pages/Friends';
import Messages from './pages/Messages';
import Album from './pages/Album';
import Settings from './pages/Settings';
import { getCurrentLanguage } from './services/i18n';
import { getCurrentTheme } from './services/theme';

function App() {
  const [language, setLanguage] = useState(getCurrentLanguage());
  const [theme, setTheme] = useState(getCurrentTheme());
  
  useEffect(() => {
    // 监听语言更改事件
    const handleLanguageChange = (event) => {
      setLanguage(event.detail);
    };
    
    // 监听主题更改事件
    const handleThemeChange = (event) => {
      setTheme(event.detail.theme);
    };
    
    window.addEventListener('languageChanged', handleLanguageChange);
    window.addEventListener('themeChanged', handleThemeChange);
    
    return () => {
      window.removeEventListener('languageChanged', handleLanguageChange);
      window.removeEventListener('themeChanged', handleThemeChange);
    };
  }, []);
  
  return (
    <Router>
      <div className="App" lang={language}>
        <Header />
        <div className="app-layout">
          <Sidebar />
          <main className="main-content">
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="/profile" element={<Profile />} />
              <Route path="/timeline" element={<Timeline />} />
              <Route path="/friends" element={<Friends />} />
              <Route path="/messages" element={<Messages />} />
              <Route path="/album" element={<Album />} />
              <Route path="/settings" element={<Settings />} />
            </Routes>
          </main>
        </div>
      </div>
    </Router>
  );
}

export default App;