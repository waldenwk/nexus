import React from 'react';
import './Home.css';

const Home = () => {
  return (
    <div className="home">
      <div className="hero">
        <h1>欢迎来到 Nexus</h1>
        <p>连接校园，分享生活</p>
      </div>
      
      <div className="features">
        <div className="feature-card">
          <h3>时间轴</h3>
          <p>查看朋友们的最新动态</p>
        </div>
        
        <div className="feature-card">
          <h3>朋友圈</h3>
          <p>与朋友保持联系，分享精彩瞬间</p>
        </div>
        
        <div className="feature-card">
          <h3>兴趣小组</h3>
          <p>加入志同道合的社群</p>
        </div>
      </div>
    </div>
  );
};

export default Home;