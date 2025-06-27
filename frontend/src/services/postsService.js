import axios from "axios";

const API_BASE = import.meta.env.VITE_ALGEBRA_SOCIAL_NETWORK_BASE_URL;

const getAuthConfig = () => ({
  headers: {
    Authorization: `Bearer ${localStorage.getItem("token")}`,
  },
});

export const getAllPosts = async (page = 0, size = 10) => {
  try {
    return await axios.get(
      `${API_BASE}/api/v1/posts?page=${page}&size=${size}`,
      getAuthConfig(),
    );
  } catch (e) {
    console.error(`Error: ${e}`);
  }
};

export const getPostById = async (id) => {
  try {
    return await axios.get(`${API_BASE}/api/v1/posts/${id}`, getAuthConfig());
  } catch (e) {
    console.error(`Error: ${e}`);
  }
};

export const createPost = async (postData) => {
  try {
    return await axios.post(
      `${API_BASE}/api/v1/posts`,
      postData,
      getAuthConfig(),
    );
  } catch (e) {
    console.error(`Error: ${e}`);
  }
};

export const deletePostById = async (postId) => {
  try {
    return await axios.delete(
      `${API_BASE}/api/v1/posts/${postId}`,
      getAuthConfig(),
    );
  } catch (e) {
    console.error(`Delete error: ${e}`);
    throw e;
  }
};

export const getPostsByUser = async (userId) => {
  try {
    return await axios.get(
      `${API_BASE}/api/v1/posts/user/${userId}`,
      getAuthConfig(),
    );
  } catch (e) {
    console.error(`Error: ${e}`);
  }
};

export const ratePost = async (id, stars) => {
  try {
    return await axios.post(
      `${API_BASE}/api/v1/posts/${id}/rate?stars=${stars}`,
      {}, // empty body, TODO check if recalculated average should be send
      getAuthConfig(),
    );
  } catch (e) {
    console.error(`Error: ${e}`);
  }
};

export const commentOnPost = async (id, content) => {
  try {
    return await axios.post(
      `${API_BASE}/api/v1/posts/${id}/comments`,
      content,
      {
        ...getAuthConfig(),
        headers: {
          ...getAuthConfig().headers,
          "Content-Type": "text/plain",
        },
      },
    );
  } catch (e) {
    console.error(`Error: ${e}`);
  }
};

export const getCommentsForPost = async (postId) => {
  try {
    return await axios.get(
      `${API_BASE}/api/v1/posts/${postId}/comments`,
      getAuthConfig(),
    );
  } catch (e) {
    console.error(`Error: ${e}`);
  }
};
