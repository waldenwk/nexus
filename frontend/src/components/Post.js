import React from 'react';
import './Post.css';

const Post = ({ post }) => {
  return (
    <div className="post-card">
      <div className="post-header">
        <img src={post.avatar} alt={`${post.username}的头像`} className="post-avatar" />
        <div className="post-user-info">
          <h3>{post.username}</h3>
          <span className="post-timestamp">{post.timestamp}</span>
        </div>
      </div>
      
      <div className="post-content">
        <div dangerouslySetInnerHTML={{ __html: post.content }} />
      </div>
      
      <div className="post-actions">
        <button className="action-button">
          👍 点赞 ({post.likes})
        </button>
        <button className="action-button">
          💬 评论 ({post.comments})
        </button>
        <button className="action-button">
          ↻ 分享
        </button>
      </div>
    </div>
  );
};

export default Post;