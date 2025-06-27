import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  Flex,
  Box,
  Image,
  Grid,
  GridItem,
  Text,
  Heading,
  Button,
  Separator,
} from "@chakra-ui/react";
import Navbar from "../components/layout/Navbar";
import PostFeed from "../components/posts/PostFeed";
import PostItem from "../components/posts/PostItem";
import { getFullName, getUID } from "../utils/utils";
import { getPostsByUser } from "../services/postsService";

function Profile() {
  const navigate = useNavigate();
  const fullName = getFullName();
  const UID = getUID();

  const handleGoToEditProfile = () => {
    navigate("/edit-profile");
  };

  const [posts, setPosts] = useState([]);

  useEffect(() => {
    const fetchPosts = async () => {
      try {
        const response = await getPostsByUser(UID);
        const comments = response.data;

        if (
          Array.isArray(comments) &&
          comments.length > 0 &&
          comments.some((c) => Object.keys(c).length > 0)
        ) {
          setPosts(comments);
        } else {
          //console.log("Nema komentara za ovaj post.");
        }
      } catch (e) {
        console.error("Greška pri dohvaćanju komentara:", e);
      }
    };

    fetchPosts();
  }, []);

  return (
    <>
      <Navbar />

      <Flex
        justify="center"
        align="center"
        bg="url(./src/assets/alg_wd_blur.svg)"
        bgRepeat="no-repeat"
        bgSize="cover"
        backgroundPosition="center"
        h="95vh"
      >
        <Flex
          direction={{ base: "column", md: "row" }}
          width="100%"
          maxW="1580px"
          height="80vh"
        >
          {/* Profile user feed */}
          <Box
            flex="1"
            border="1px solid black"
            bg="white"
            p={4}
            className="feed-scroll"
          >
            <Grid templateColumns="auto 1fr" gap={4} padding={10}>
              <GridItem>
                <Image
                  rounded="md"
                  height={200}
                  src="https://avatars.githubusercontent.com/u/210037477?v=4"
                  alt={fullName}
                  marginRight={5}
                />
              </GridItem>
              <GridItem>
                <Heading fontSize={"xl"} fontFamily={"body"}>
                  {fullName}
                </Heading>
                <Text fontWeight={600} color={"gray.500"} mb={4}>
                  Programsko inženjerstvo
                </Text>
                <Separator padding={2} />
                <Button
                  tariant="outline"
                  style={{
                    background: "linear-gradient(45deg, #2563eb, #3b82f6)",
                    color: "white",
                  }}
                  size="sm"
                  onClick={handleGoToEditProfile}
                >
                  Edit profile
                </Button>
              </GridItem>
            </Grid>

            {posts.map((post) => (
              <PostItem key={post.id} post={post} />
            ))}
          </Box>
        </Flex>
      </Flex>
    </>
  );
}

export default Profile;
