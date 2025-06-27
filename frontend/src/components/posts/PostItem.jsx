import React, { useState } from "react";
import {
  Box,
  Text,
  Avatar,
  VStack,
  HStack,
  Textarea,
  Button,
} from "@chakra-ui/react";
import { FaStar, FaRegStar, FaRegCommentDots } from "react-icons/fa";
import { formatDate } from "../../utils/utils";

const PostItem = ({ post }) => {
  const [rating, setRating] = useState(0);
  const [comment, setComment] = useState("");
  const [comments, setComments] = useState([]);

  const handleRating = (value) => setRating(value);

  const handleAddComment = () => {
    if (comment.trim()) {
      setComments([...comments, comment]);
      setComment("");
    }
  };

  return (
    <Box
      bg="white"
      borderRadius="2xl"
      boxShadow="lg"
      p={6}
      w="100%"
      mb={6}
      border="1px solid rgba(0,0,0,0.05)"
    >
      <HStack mb={4} spacing={4}>
        <Avatar.Root
          size={"lg"}
          mb={4}
          pos={"relative"}
          _after={{
            content: '""',
            w: 5,
            h: 5,
            bg: "green.300",
            border: "2px solid white",
            rounded: "full",
            pos: "absolute",
            bottom: -1,
            right: 0,
          }}
        >
          <Avatar.Fallback name="Ime Prezime" />
          <Avatar.Image src="https://avatars.githubusercontent.com/u/210037477?v=4" />
        </Avatar.Root>
        <VStack spacing={0} align="start" color="black">
          <Text fontWeight="bold">
            {post?.userFullName || "Full Name"}
          </Text>
          <Text fontSize="sm" color="gray.500" style={{ marginTop: -5 }}>
            {formatDate(post?.createdAt) || "Posted just now"}
          </Text>
        </VStack>
      </HStack>

      <Text fontSize="lg" fontWeight="semibold" mb={1} color="black">
        {post?.title || "Post Title"}
      </Text>
      <Text fontSize="md" color="gray.700" mb={4}>
        {post?.content || "Post content"}
      </Text>

      <Box mb={4}>
        <Text fontWeight="medium" mb={2} color="black">
          Your Rating:
        </Text>
        <HStack spacing={1}>
          {[1, 2, 3, 4, 5].map((val) =>
            val <= rating ? (
              <FaStar
                key={val}
                color="gold"
                cursor="pointer"
                onClick={() => handleRating(val)}
              />
            ) : (
              <FaRegStar
                key={val}
                color="gray"
                cursor="pointer"
                onClick={() => handleRating(val)}
              />
            ),
          )}
        </HStack>
      </Box>

      <Box mt={4}>
        <Text fontWeight="medium" mb={2} color="black">
          Comments:
        </Text>
        <VStack spacing={2} align="start" mb={2}>
          {comments.map((cmt, i) => (
            <Box key={i} px={3} py={2} bg="gray.100" borderRadius="md" w="100%">
              <Text>{cmt}</Text>
            </Box>
          ))}
        </VStack>
        <Textarea
          value={comment}
          onChange={(e) => setComment(e.target.value)}
          placeholder="Write a comment..."
          size="sm"
          mb={2}
        />
        <HStack justify="flex-end">
          <Button
            size="sm"
            onClick={handleAddComment}
            leftIcon={<FaRegCommentDots />}
            bg="linear-gradient(90deg, var(--alg-gradient-color-1), var(--alg-gradient-color-2))"
            color="white"
            _hover={{
              opacity: 0.9,
            }}
          >
            Add Comment
          </Button>
        </HStack>
      </Box>
    </Box>
  );
};

export default PostItem;
