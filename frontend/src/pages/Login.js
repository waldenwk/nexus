import React, { useState } from 'react';
import ApiService from '../services/api';
import { t } from '../services/i18n';
import './Login.css';

const Login = () => {
  const [formData, setFormData] = useState({
    email: '',
    password: ''
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const { email, password } = formData;

  const onChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const onSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    
    try {
      const response = await ApiService.login({ email, password });
      if (response.success) {
        // 保存token到localStorage
        localStorage.setItem('token', response.data.token);
        // 重定向到首页
        window.location.href = '/';
      } else {
        setError(t('error.login'));
      }
    } catch (err) {
      setError(t('error.network'));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login">
      <div className="login-container">
        <h2>{t('auth.login')}</h2>
        {error && <div className="error-message">{error}</div>}
        <form onSubmit={onSubmit} className="login-form">
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
          
          <button type="submit" className="login-btn" disabled={loading}>
            {loading ? t('common.loading') : t('auth.login')}
          </button>
        </form>
        
        <div className="login-footer">
          <p>{t('auth.noAccount')} <a href="/register">{t('auth.register')}</a></p>
        </div>
      </div>
    </div>
  );
};

export default Login;