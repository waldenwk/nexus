import React, { useState } from 'react';
import ApiService from '../services/api';
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
      setError('两次输入的密码不一致');
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
        setError('注册失败，请稍后重试');
      }
    } catch (err) {
      setError('注册过程中发生错误，请稍后重试');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="register">
      <div className="register-container">
        <h2>创建 Nexus 账户</h2>
        {error && <div className="error-message">{error}</div>}
        <form onSubmit={onSubmit} className="register-form">
          <div className="form-group">
            <input
              type="text"
              name="username"
              value={username}
              onChange={onChange}
              placeholder="用户名"
              required
            />
          </div>
          
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
          
          <div className="form-group">
            <input
              type="password"
              name="password2"
              value={password2}
              onChange={onChange}
              placeholder="确认密码"
              required
            />
          </div>
          
          <button type="submit" className="register-btn" disabled={loading}>
            {loading ? '注册中...' : '注册'}
          </button>
        </form>
        
        <div className="register-footer">
          <p>已有账户? <a href="/login">立即登录</a></p>
        </div>
      </div>
    </div>
  );
};

export default Register;