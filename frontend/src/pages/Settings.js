import React, { useState, useEffect } from 'react';
import { t, getCurrentLanguage, setLanguage, getSupportedLanguages } from '../services/i18n';
import { getCurrentTheme, getSupportedThemes, getThemeIcon, setTheme } from '../services/theme';
import './Settings.css';

const Settings = () => {
  const [currentLanguage, setCurrentLanguage] = useState(getCurrentLanguage());
  const [currentTheme, setCurrentTheme] = useState(getCurrentTheme());
  
  useEffect(() => {
    // 监听语言更改事件
    const handleLanguageChange = (event) => {
      setCurrentLanguage(event.detail);
    };
    
    // 监听主题更改事件
    const handleThemeChange = (event) => {
      setCurrentTheme(event.detail.theme);
    };
    
    window.addEventListener('languageChanged', handleLanguageChange);
    window.addEventListener('themeChanged', handleThemeChange);
    
    return () => {
      window.removeEventListener('languageChanged', handleLanguageChange);
      window.removeEventListener('themeChanged', handleThemeChange);
    };
  }, []);
  
  const handleLanguageChange = (event) => {
    const newLanguage = event.target.value;
    setLanguage(newLanguage);
  };
  
  const handleThemeChange = (event) => {
    const newTheme = event.target.value;
    setTheme(newTheme);
  };
  
  const languageOptions = {
    'zh': '中文',
    'en': 'English'
  };
  
  return (
    <div className="settings">
      <div className="settings-container">
        <h2>{t('settings.title')}</h2>
        
        <div className="settings-section">
          <h3>{t('settings.language')}</h3>
          <div className="settings-item">
            <label htmlFor="language-select">{t('settings.language')}:</label>
            <select 
              id="language-select"
              value={currentLanguage} 
              onChange={handleLanguageChange}
              className="settings-select"
            >
              {getSupportedLanguages().map(lang => (
                <option key={lang} value={lang}>
                  {languageOptions[lang]}
                </option>
              ))}
            </select>
          </div>
        </div>
        
        <div className="settings-section">
          <h3>{t('settings.theme')}</h3>
          <div className="settings-item">
            <label htmlFor="theme-select">{t('settings.theme')}:</label>
            <select 
              id="theme-select"
              value={currentTheme} 
              onChange={handleThemeChange}
              className="settings-select"
            >
              {getSupportedThemes().map(theme => (
                <option key={theme} value={theme}>
                  {getThemeIcon(theme)} {theme.charAt(0).toUpperCase() + theme.slice(1)}
                </option>
              ))}
            </select>
          </div>
        </div>
        
        <div className="settings-section">
          <h3>{t('settings.notifications')}</h3>
          <div className="settings-item">
            <label>
              <input type="checkbox" defaultChecked /> 
              {t('settings.notifications')}
            </label>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Settings;