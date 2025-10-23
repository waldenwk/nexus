import React, { useState } from 'react';
import ApiService from '../services/api';
import { t } from '../services/i18n';
import './Register.css';

const Register = () => {
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    password2: ''
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const { username, email, password, password2 } = formData;

  const onChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const onSubmit = async (e) => {
    e.preventDefault();
    
    if (password !== password2) {
      setError(t('error.passwordMismatch'));
      return;
    }
    
    setLoading(true);
    setError('');
    
    try {
      const response = await ApiService.register({ username, email, password });
      if (response.success) {
        // 保存token到localStorage
        localStorage.setItem('token', response.data.token);
        // 重定向到首页
        window.location.href = '/';
      } else {
        setError(t('error.register'));
      }
    } catch (err) {
      setError(t('error.network'));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="register">
      <div className="register-container">
        <h2>{t('auth.register')}</h2>
        {error && <div className="error-message">{error}</div>}
        <form onSubmit={onSubmit} className="register-form">
          <div className="form-group">
            <input
              type="text"
              name="username"
              value={username}
              onChange={onChange}
              placeholder={t('auth.username')}
              required
            />
          </div>
          
          <div className="form-group">
            <input
              type="email"
              name="email"
              value={email}
              onChange={onChange}
              placeholder={t('auth.email')}
              required
            />
          </div>
          
          <div className="form-group">
            <input
              type="password"
              name="password"
              value={password}
              onChange={onChange}
              placeholder={t('auth.password')}
              required
            />
          </div>
          
          <div className="form-group">
            <input
              type="password"
              name="password2"
              value={password2}
              onChange={onChange}
              placeholder={t('auth.confirmPassword')}
              required
            />
          </div>
          
          <button type="submit" className="register-btn" disabled={loading}>
            {loading ? t('common.loading') : t('auth.register')}
          </button>
        </form>
        
        <div className="register-footer">
          <p>{t('auth.hasAccount')} <a href="/login">{t('auth.login')}</a></p>
        </div>
      </div>
    </div>
  );
};

export default Register;