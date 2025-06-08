import React, { useEffect, useState } from "react";
import {
    Box,
    Heading,
    VStack,
    Text,
    Button,
    HStack,
    Spinner,
    Flex,
} from "@chakra-ui/react";
import {
    getPendingFriendRequests,
    approveFriendRequest,
    declineFriendRequest,
} from "../services/friendsService.js";

import Navbar from "../components/layout/Navbar.jsx";
import Sidebar from "../components/layout/Sidebar.jsx";
import AlgBG from "../assets/alg_wd_blur.svg";

const RequestsPage = () => {
    const [requests, setRequests] = useState([]);
    const [loading, setLoading] = useState(true);

    const fetchRequests = async () => {
        try {
            const res = await getPendingFriendRequests();
            setRequests(res.data || []);
        } catch (err) {
            alert("Error fetching requests.");
        } finally {
            setLoading(false);
        }
    };

    const handleApprove = async (id) => {
        try {
            await approveFriendRequest(id);
            alert("Friend request approved.");
            fetchRequests();
        } catch (err) {
            alert("Error approving request.");
        }
    };

    const handleDecline = async (id) => {
        try {
            await declineFriendRequest(id);
            alert("Friend request declined.");
            fetchRequests();
        } catch (err) {
            alert("Error declining request.");
        }
    };

    useEffect(() => {
        fetchRequests();
    }, []);

    if (loading) return <Spinner size="xl" mt={10} />;

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
                    <Box width={{ base: "100%", md: "20%" }} marginRight={5}>
                        <Sidebar />
                    </Box>

                    <Box flex="1" p={6} overflowY="auto">
                        <Heading size="lg" mb={4} color="white">
                            Pending Friend Requests
                        </Heading>
                        <VStack spacing={4} align="stretch">
                            {requests.length === 0 ? (
                                <Text color="white">No pending friend requests</Text>
                            ) : (
                                requests.map((req) => (
                                    <Box
                                        key={req.id}
                                        p={4}
                                        shadow="md"
                                        borderWidth="1px"
                                        rounded="md"
                                        bg="whiteAlpha.800"
                                    >
                                        <HStack justifyContent="space-between">
                                            <Text
                                                fontWeight="bold"
                                                bg="linear-gradient(45deg, var(--alg-gradient-color-1), var(--alg-gradient-color-2))"
                                                bgClip="text"
                                                color="transparent"
                                            >
                                                {req.senderFullName || "Unknown User"}
                                            </Text>
                                            <HStack>
                                                <Button
                                                    colorScheme="green"
                                                    size="sm"
                                                    onClick={() => handleApprove(req.id)}
                                                >
                                                    Approve
                                                </Button>
                                                <Button
                                                    colorScheme="red"
                                                    size="sm"
                                                    onClick={() => handleDecline(req.id)}
                                                >
                                                    Decline
                                                </Button>
                                            </HStack>
                                        </HStack>
                                    </Box>
                                ))
                            )}
                        </VStack>
                    </Box>
                </Flex>
            </Flex>
        </>
    );
};

export default RequestsPage;
