import {
  Avatar,
  Input,
  Stack,
  Box,
  VStack,
  Textarea,
  Button,
  Flex,
} from "@chakra-ui/react";
import React, { useState } from "react";
import { getFullName } from "../../utils/utils";
import { createPost, uploadPostImage } from "../../services/postsService";

function PostFeed({ onPostCreated }) {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [selectedFile, setSelectedFile] = useState(null);
  const fullName = getFullName();

  const handlePost = async () => {
    if (!title.trim() && !content.trim()) return;

    console.log("SUBMITTING POST:", { title, content }); // 🔍 debug check

    const postData = {
      title,
      content,
      imageId: " ", // placeholder
    };

    try {
      const response = await createPost(postData);

      if (response?.data?.id) {
        const postId = response.data.id;

        // Upload image if selected
        if (selectedFile) {
          const formData = new FormData();
          formData.append("file", selectedFile);

          try {
            await uploadPostImage(postId, formData);
            console.log("Image uploaded");
          } catch (uploadError) {
            console.error("Image upload failed:", uploadError);
          }
        }

        // Reset form
        setTitle("");
        setContent("");
        setSelectedFile(null);
        if (typeof onPostCreated === "function") onPostCreated();
      } else {
        console.log("No response from posting.");
      }
    } catch (e) {
      console.log("Post error:", e);
    }
  };

  return (
    <Box display="flex" align="center" justifyContent="center">
      <Stack
        direction="row"
        w="100%"
        borderBottom="1px solid var(--feed-div)"
        spacing={6}
        py={4}
      >
        <Box display="flex" alignItems="center" justifyContent="center">
          <Avatar.Root size="2xl" pos="relative">
            <Avatar.Fallback name={fullName} />
            <Avatar.Image src="https://avatars.githubusercontent.com/u/210037477?v=4" />
          </Avatar.Root>
        </Box>

        <VStack spacing={4} w="100%">
          <Box w="100%">
            <Input
              value={title}
              placeholder="Feed title"
              mb={2}
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
            <Box mt={3}>
              <input
                type="file"
                accept=".jpg,.jpeg,.png"
                onChange={(e) => {
                  if (e.target.files?.[0]) {
                    setSelectedFile(e.target.files[0]);
                  }
                }}
              />
            </Box>

            {selectedFile && (
              <Box mt={3}>
                <img
                  src={URL.createObjectURL(selectedFile)}
                  alt="Preview"
                  style={{ maxHeight: "150px", borderRadius: "8px" }}
                />
              </Box>
            )}

            <Flex justify="flex-end" mt={4}>
              <Button
                variant="outline"
                bgGradient="linear(to-r, var(--alg-gradient-color-1), var(--alg-gradient-color-2))"
                color="white"
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
