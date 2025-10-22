import React, { useState } from 'react';
import ApiService from '../services/api';
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
        setError('登录失败，请检查邮箱和密码');
      }
    } catch (err) {
      setError('登录过程中发生错误，请稍后重试');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login">
      <div className="login-container">
        <h2>登录到 Nexus</h2>
        {error && <div className="error-message">{error}</div>}
        <form onSubmit={onSubmit} className="login-form">
          <div className="form-group">
            <input
              type="email"
              name="email"
              value={email}
              onChange={onChange}
              placeholder="邮箱地址"
              required
            />
          </div>
          
          <div className="form-group">
            <input
              type="password"
              name="password"
              value={password}
              onChange={onChange}
              placeholder="密码"
              required
            />
          </div>
          
          <button type="submit" className="login-btn" disabled={loading}>
            {loading ? '登录中...' : '登录'}
          </button>
        </form>
        
        <div className="login-footer">
          <p>还没有账户? <a href="/register">立即注册</a></p>
        </div>
      </div>
    </div>
  );
};

export default Login;