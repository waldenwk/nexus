import React, { useState, useEffect } from 'react';
import ApiService from '../services/api';
import Post from '../components/Post';
import PostEditor from '../components/PostEditor';
import './Timeline.css';

const Timeline = () => {
  const [posts, setPosts] = useState([]);
  const [newPost, setNewPost] = useState('');
  const [editorState, setEditorState] = useState(null);
  const [loading, setLoading] = useState(true);
  const [posting, setPosting] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchTimeline();
  }, []);

  const fetchTimeline = async () => {
    try {
      setLoading(true);
      const response = await ApiService.getTimeline();
      if (response.success) {
        setPosts(response.data);
      } else {
        setError('获取时间轴失败');
      }
    } catch (err) {
      setError('获取时间轴时发生错误');
    } finally {
      setLoading(false);
    }
  };

  const handlePostContentChange = (content, editorState) => {
    setNewPost(content);
    setEditorState(editorState);
  };

  const handlePostSubmit = async (e) => {
    e.preventDefault();
    if (newPost.trim() === '') return;

    try {
      setPosting(true);
      const response = await ApiService.createPost(newPost);
      if (response.success) {
        setPosts([response.data, ...posts]);
        setNewPost('');
      } else {
        setError('发布失败，请稍后重试');
      }
    } catch (err) {
      setError('发布过程中发生错误');
    } finally {
      setPosting(false);
    }
  };

  if (loading) {
    return <div className="timeline">加载中...</div>;
  }

  return (
    <div className="timeline">
      <div className="timeline-header">
        <h1>时间轴</h1>
      </div>

      <div className="post-form-container">
        {error && <div className="error-message">{error}</div>}
        <form onSubmit={handlePostSubmit} className="post-form">
          <PostEditor
            initialValue={newPost}
            onChange={handlePostContentChange}
            placeholder="分享你的新鲜事..."
          />
          <button type="submit" className="post-button" disabled={posting}>
            {posting ? '发布中...' : '发布'}
          </button>
        </form>
      </div>

      <div className="posts-container">
        {posts.map(post => (
          <Post key={post.id} post={post} />
        ))}
      </div>
    </div>
  );
};

export default Timeline;