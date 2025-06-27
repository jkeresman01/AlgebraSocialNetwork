import {
  Avatar,
  Input,
  Stack,
  Text,
  Box,
  VStack,
  Textarea,
  Button,
  Flex,
} from "@chakra-ui/react";
import React, { useEffect, useState } from "react";
import { getFullName } from "../../utils/utils";
import { createPost } from "../../services/postsService";

function PostFeed({ onPostCreated }) {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const fullName = getFullName();

  const handlePost = async () => {
    if (!title.trim() && !content.trim()) return;

    const postData = {
      title,
      content,
      imageId: " ",
    };

    try {
      const response = await createPost(postData);

      if (response?.data) {
        //console.log('Post created:', response.data);
        setTitle("");
        setContent("");
        if (typeof onPostCreated === "function") onPostCreated();
      } else {
        console.log("No response from posting.");
      }
    } catch (e) {
      console.log("Post error:", e);
    }
  };

  return (
    <Box
      display="flex"
      align="center"
      alignItems="center"
      justifyContent="center"
    >
      <Stack
        direction="row"
        h="auto"
        w="100%"
        borderBottom={"1px solid var(--feed-div)"}
      >
        <Box
          display="flex"
          align="center"
          alignItems="center"
          justifyContent="center"
        >
          <Avatar.Root size={"2xl"} pos={"relative"}>
            <Avatar.Fallback name={fullName} />
            <Avatar.Image src="https://avatars.githubusercontent.com/u/210037477?v=4" />
          </Avatar.Root>
        </Box>

        <VStack spacing={4} w="100%">
          <Box p={4} w="100%">
            <Input
              value={title}
              placeholder="Feed title"
              flex="1"
              marginBottom={"2"}
              onChange={(e) => setTitle(e.target.value)}
            />
            <Textarea
              placeholder="What's on your mind..."
              minHeight="100px"
              height="100px"
              maxHeight="200px"
              w="100%"
              value={content}
              onChange={(e) => setContent(e.target.value)}
            />
            <Flex w="100%" justify="flex-end">
              <Button
                variant="outline"
                style={{
                  background:
                    "linear-gradient(45deg, var(--alg-gradient-color-1), var(--alg-gradient-color-2))",
                  color: "white",
                }}
                onClick={handlePost}
              >
                Post
              </Button>
            </Flex>
          </Box>
        </VStack>
      </Stack>
    </Box>
  );
}

export default PostFeed;
