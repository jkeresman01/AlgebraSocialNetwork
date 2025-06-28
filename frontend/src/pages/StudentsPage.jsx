import React, { useEffect, useState } from "react";
import { Box, Button, Flex, Spinner, Text, VStack } from "@chakra-ui/react";
import Navbar from "../components/layout/Navbar.jsx";
import Sidebar from "../components/layout/Sidebar.jsx";
import AlgBG from "../assets/alg_wd_blur.svg";
import { sendFriendRequest, getNonFriends } from "../services/friendsService";

const StudentsPage = () => {
  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [sending, setSending] = useState({});

  useEffect(() => {
    document.body.style.background = `url(${AlgBG})`;
    document.body.style.backgroundSize = "cover";
    document.body.style.backgroundPosition = "center";
    return () => {
      document.body.style.background = "";
    };
  }, []);

  const fetchStudents = async () => {
    try {
      const res = await getNonFriends();
      setStudents(res?.data || []);
    } catch (e) {
      console.error("Failed to fetch non-friends", e);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchStudents();
  }, []);

  const handleAddFriend = async (id) => {
    setSending((prev) => ({ ...prev, [id]: true }));
    try {
      await sendFriendRequest(id);
      alert("Friend request sent.");
      fetchStudents(); // Refresh list
    } catch (e) {
      console.error("Error sending friend request", e);
      alert("Failed to send request.");
    } finally {
      setSending((prev) => ({ ...prev, [id]: false }));
    }
  };

  return (
    <>
      <Navbar />

      <Flex
        justify="center"
        align="center"
        bg={`url(${AlgBG})`}
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
          <Box width={{ base: "100%", md: "20%" }} mr={5}>
            <Sidebar />
          </Box>

          <Box flex="1" overflowY="auto">
            {loading ? (
              <Spinner size="xl" mt={10} />
            ) : (
              <VStack spacing={4}>
                {students.length === 0 ? (
                  <Text>No users to add.</Text>
                ) : (
                  students.map((student) => (
                    <Box
                      key={student.id}
                      p={4}
                      width="100%"
                      shadow="md"
                      borderWidth="1px"
                      bg="whiteAlpha.800"
                      rounded="lg"
                    >
                      <Flex justifyContent="space-between" alignItems="center">
                        <Box>
                          <Text
                            fontWeight="bold"
                            bg="linear-gradient(45deg, var(--alg-gradient-color-1), var(--alg-gradient-color-2))"
                            bgClip="text"
                            color="transparent"
                          >
                            {student.firstName} {student.lastName}
                          </Text>
                          <Text fontSize="sm" color="gray.600">
                            {student.email}
                          </Text>
                        </Box>

                        <Button
                          style={{
                            background:
                              "linear-gradient(45deg, var(--alg-gradient-color-1), var(--alg-gradient-color-2))",
                            color: "white",
                          }}
                          size="sm"
                          isLoading={sending[student.id]}
                          onClick={() => handleAddFriend(student.id)}
                        >
                          Add Friend
                        </Button>
                      </Flex>
                    </Box>
                  ))
                )}
              </VStack>
            )}
          </Box>
        </Flex>
      </Flex>
    </>
  );
};

export default StudentsPage;
