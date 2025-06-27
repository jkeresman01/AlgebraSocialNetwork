import React, { useEffect, useState } from "react";
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
import { formatDate, getUID } from "../../utils/utils";
import {
  commentOnPost,
  ratePost,
  getCommentsForPost,
  deletePostById,
} from "../../services/postsService";
import CommentItem from "./CommentItem";

const PostItem = ({ post }) => {
  const [rating, setRating] = useState(0);
  const [comment, setComment] = useState("");
  const [comments, setComments] = useState([]);
  const UID = getUID();

  const handleRating = (value) => {
    const response = ratePost(post.id, value);
    //console.log("[PostRate]: " + response);
    setRating(value);
  };

  const handleAddComment = async () => {
    if (!comment.trim()) return;

    try {
      await commentOnPost(post.id, comment);
      setComment("");

      const response = await getCommentsForPost(post.id);
      const updatedComments = response.data;
      if (Array.isArray(updatedComments)) {
        setComments(updatedComments);
      }
    } catch (e) {
      console.log("Comment error:", e);
    }
  };

  const handleDeletePost = async () => {
    console.log("Post: " + post.id);
    try {
      await deletePostById(post.id);
    } catch (e) {
      console.log("Failed to delete post. " + e);
    }
  };

  useEffect(() => {
    setRating(post.averageRating);

    const fetchComments = async () => {
      try {
        const response = await getCommentsForPost(post.id);
        const comments = response.data;

        if (
          Array.isArray(comments) &&
          comments.length > 0 &&
          comments.some((c) => Object.keys(c).length > 0)
        ) {
          setComments(comments);
        } else {
          //console.log("Nema komentara za ovaj post.");
        }
      } catch (e) {
        console.error("Greška pri dohvaćanju komentara:", e);
      }
    };
    fetchComments();
  }, [post.id]);

  //console.log(comments);
  //console.count(`PostItem render - post ID: ${post.id}`);

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
            {post?.userFullName || "Greska u imenu"}
          </Text>
          <Text fontSize="sm" color="gray.500" style={{ marginTop: -5 }}>
            {formatDate(post?.createdAt) || "Posted just now"}
          </Text>
        </VStack>
      </HStack>

      <Text fontSize="lg" fontWeight="semibold" mb={1} color="black">
        {post?.title || "Greska u post titleu"}
      </Text>
      <Text fontSize="md" color="gray.700" mb={4}>
        {post?.content || "Greska u post contentu"}
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
        <VStack spacing={3} align="start" mb={2} w="100%">
          {Array.isArray(comments) &&
            comments.map((cmt, i) => <CommentItem key={i} comment={cmt} />)}
        </VStack>
        <Textarea
          value={comment}
          onChange={(e) => setComment(e.target.value)}
          placeholder="Write a comment..."
          size="sm"
          mb={2}
        />
        <HStack justify="flex-end">
          {UID === post.userId && (
            <Button
              size="sm"
              onClick={handleDeletePost}
              leftIcon={<FaRegCommentDots />}
              bg="linear-gradient(90deg, #2f3144, #555879)"
              color="white"
              _hover={{
                opacity: 0.9,
              }}
            >
              Delete post
            </Button>
          )}

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
