import React from 'react';
import { Flex, Box } from '@chakra-ui/react';
import Navbar from '../components/layout/Navbar.jsx';
import Sidebar from '../components/layout/Sidebar.jsx';
import PostFeed from '../components/posts/PostFeed.jsx';
import PostItem from '../components/posts/PostItem.jsx';

function HomePage() {
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
                    direction={{ base: 'column', md: 'row' }}
                    width="100%"
                    maxW="1600px"
                    height="80vh"
                    gap={6}
                >
                    <Box
                        width={{ base: '100%', md: '22%' }}
                        mb={{ base: 6, md: 0 }}
                    >
                        <Sidebar />
                    </Box>

                    <Box
                        flex="1"
                        p={6}
                        bg="white" // whiteAlpha.100
                        borderRadius="lg"
                        boxShadow="lg"
                        minH="80vh"
                        overflowY="auto"
                        className="feed-scroll"
                    >
                        
                            <PostFeed />
                            <PostItem />
                            <PostItem />
                            <PostItem />
                            <PostItem />
                            <PostItem />
                            <PostItem />
                    </Box>

                </Flex>
            </Flex>
        </>
    );
}

function HomePageX() {
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
                    direction={{ base: 'column', md: 'row' }}
                    width="100%"
                    maxW="1600px"
                    gap={6}
                >
                    <Box
                        width={{ base: '100%', md: '22%' }}
                        mb={{ base: 6, md: 0 }}
                    >
                        <Sidebar />
                    </Box>

                    <Box
                        flex="1"

                        bg="whiteAlpha.100"
                        borderRadius="lg"
                        boxShadow="lg"
                        border="1px solid rgba(255,255,255,0.2)"
                        minH="80vh"
                        overflowY="auto"
                        className="feed-scroll"
                    >
                        <PostFeed />
                        <Box
                            mt={6}
                            maxH="calc(100vh - 200px)"
                            overflowY="auto"
                            pr={2}
                            className="feed-scroll"
                        >
                            <PostItem />
                            <PostItem />
                            <PostItem />
                            <PostItem />
                        </Box>
                    </Box>
                </Flex>
            </Flex>
        </>
    );
}

export default HomePage;
