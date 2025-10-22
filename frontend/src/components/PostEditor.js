import React, { useState } from 'react';
import { EditorState, convertToRaw, ContentState } from 'draft-js';
import { Editor } from 'react-draft-wysiwyg';
import draftToHtml from 'draftjs-to-html';
import htmlToDraft from 'html-to-draftjs';
import 'react-draft-wysiwyg/dist/react-draft-wysiwyg.css';
import './PostEditor.css';

const PostEditor = ({ initialValue = '', onChange, placeholder = '写点什么...' }) => {
  const [editorState, setEditorState] = useState(() => {
    if (initialValue) {
      const blocksFromHtml = htmlToDraft(initialValue);
      if (blocksFromHtml) {
        const { contentBlocks, entityMap } = blocksFromHtml;
        const contentState = ContentState.createFromBlockArray(contentBlocks, entityMap);
        return EditorState.createWithContent(contentState);
      }
    }
    return EditorState.createEmpty();
  });

  const onEditorStateChange = (editorState) => {
    setEditorState(editorState);
    
    // Convert to HTML and pass to parent component
    const contentState = editorState.getCurrentContent();
    const rawContentState = convertToRaw(contentState);
    const html = draftToHtml(rawContentState);
    
    if (onChange) {
      onChange(html, editorState);
    }
  };

  return (
    <div className="post-editor">
      <Editor
        editorState={editorState}
        onEditorStateChange={onEditorStateChange}
        placeholder={placeholder}
        toolbar={{
          options: ['inline', 'blockType', 'fontSize', 'list', 'textAlign', 'history'],
          inline: {
            options: ['bold', 'italic', 'underline', 'strikethrough'],
          },
          blockType: {
            options: ['Normal', 'H1', 'H2', 'H3', 'Blockquote'],
          },
        }}
      />
    </div>
  );
};

export default PostEditor;