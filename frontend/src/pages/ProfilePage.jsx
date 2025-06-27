import React from "react";
import {
  Flex,
  Box,
  Image,
  Grid,
  GridItem,
  Text,
  Heading,
} from "@chakra-ui/react";
import Navbar from "../components/layout/Navbar";
import PostItem from "../components/posts/PostItem";
import { getFullName } from "../utils/utils";

function Profile() {
  const fullName = getFullName();

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
        minH="95vh"
        px={4}
      >
        <Flex
          direction={{ base: "column", md: "row" }}
          width="100%"
          maxW="1580px"
          height="80vh"
        >
          {/* Profile content */}
          <Box
            flex="1"
            border="1px solid black"
            bg="white"
            p={6}
            className="feed-scroll"
            borderRadius="lg"
            boxShadow="lg"
            overflowY="auto"
          >
            <Grid templateColumns="auto 1fr" gap={4} padding={4}>
              <GridItem>
                <Image
                  rounded="md"
                  src="https://avatars.githubusercontent.com/u/210037477?v=4"
                  alt="User Avatar"
                />
              </GridItem>
              <GridItem>
                <Heading fontSize="xl" fontFamily="body">
                  {fullName}
                </Heading>
                <Text fontWeight={600} color="gray.500" mb={4}>
                  Programsko inženjerstvo
                </Text>
                <Text fontWeight="bold">Bruno Koren</Text>
                <Text>
                  Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                  Mauris rutrum, erat in ultrices posuere, erat nulla volutpat
                  eros, sed sollicitudin velit quam id metus. Integer efficitur
                  dui vitae ex cursus, nec pharetra ligula egestas. Etiam
                  finibus massa libero, eu mollis diam hendrerit a. Ut ac odio
                  quis nunc condimentum lacinia at ac lacus. Quisque id sem
                  massa. Sed vulputate pharetra ligula, vitae porta lorem
                  bibendum a. Ut mollis molestie libero, id vulputate felis
                  condimentum in.
                </Text>
              </GridItem>
            </Grid>

            <PostItem />
            <PostItem />
            <PostItem />
          </Box>
        </Flex>
      </Flex>
    </>
  );
}

export default Profile;
