import React, { useEffect, useState } from "react";
import { Flex, Box, Button, HStack } from "@chakra-ui/react";
import Navbar from "../components/layout/Navbar.jsx";
import Sidebar from "../components/layout/Sidebar.jsx";
import PostFeed from "../components/posts/PostFeed.jsx";
import PostItem from "../components/posts/PostItem.jsx";
import { getAllPosts, getFriendsPosts } from "../services/postsService.js";

function HomePage() {
  const [posts, setPosts] = useState([]);
  const [showFriendsPosts, setShowFriendsPosts] = useState(false);

  const fetchPosts = async () => {
    try {
      const response = showFriendsPosts
        ? await getFriendsPosts()
        : await getAllPosts();
      setPosts(response?.data?.content || []);
    } catch (err) {
      console.error("Failed to load posts:", err);
    }
  };

  useEffect(() => {
    fetchPosts();
  }, [showFriendsPosts]);

  const handlePostCreated = () => {
    fetchPosts();
  };

  const activeButtonBg = "#3182CE";
  const inactiveButtonBg = "#EDF2F7";

  return (
    <>
      <Navbar />
      <Flex
        justify="center"
        align="start"
        bg="url(./src/assets/alg_wd_blur.svg)"
        bgRepeat="no-repeat"
        bgSize="cover"
        backgroundPosition="center"
        minH="100vh"
        pt={6}
        px={4}
      >
        <Flex
          direction={{ base: "column", md: "row" }}
          width="100%"
          maxW="1600px"
          gap={6}
        >
          <Box width={{ base: "100%", md: "22%" }} mb={{ base: 6, md: 0 }}>
            <Sidebar />
          </Box>

          <Box flex="1" minH="80vh">
            {/* Post Feed (Create new post) */}
            <Box
              bg="white"
              borderRadius="lg"
              boxShadow="lg"
              border="1px solid rgba(255,255,255,0.2)"
              p={6}
              mb={4}
            >
              <PostFeed onPostCreated={handlePostCreated} />
            </Box>

            {/* Toggle Buttons */}
            <HStack mb={4} spacing={4}>
              <Button
                colorScheme="blue"
                bg={!showFriendsPosts ? activeButtonBg : inactiveButtonBg}
                color={!showFriendsPosts ? "white" : "black"}
                onClick={() => setShowFriendsPosts(false)}
              >
                All posts
              </Button>
              <Button
                colorScheme="blue"
                bg={showFriendsPosts ? activeButtonBg : inactiveButtonBg}
                color={showFriendsPosts ? "white" : "black"}
                onClick={() => setShowFriendsPosts(true)}
              >
                Friends posts
              </Button>
            </HStack>

            {/* Posts Feed */}
            <Box
              className="feed-scroll"
              maxH="calc(100vh - 300px)"
              overflowY="auto"
            >
              {posts.map((post) => (
                <PostItem key={post.id} post={post} />
              ))}
            </Box>
          </Box>
        </Flex>
      </Flex>
    </>
  );
}

export default HomePage;
