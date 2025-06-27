// components/CommentItem.jsx
import React from "react";
import { Box, Text, Avatar, HStack, VStack, Image } from "@chakra-ui/react";
import { formatDate } from "../../utils/utils";

const CommentItem = ({ comment }) => {
  return (
    <HStack align="start" spacing={3} w="100%">
      <Avatar.Root size="lg" mt={3}>
        <Avatar.Fallback name={comment.userFullName} />
        <Avatar.Image src="https://avatars.githubusercontent.com/u/210037477?v=4" />{" "}
        {/* {comment.userAvatarUrl} -> Preko USERID-a moram doci do podataka za avatar i ime*/}
      </Avatar.Root>
      <VStack align="start" spacing={0} w="100%">
        <HStack spacing={2}>
          <Text fontWeight="bold">{comment.userFullName}</Text>
          <Text fontSize="xs" color="gray.500">
            {formatDate(comment.createdAt)}
          </Text>
        </HStack>
        <Box bg="gray.100" px={3} py={2} borderRadius="md" w="100%">
          <Text fontSize="sm">{comment.content}</Text>
        </Box>
      </VStack>
    </HStack>
  );
};

export default CommentItem;
