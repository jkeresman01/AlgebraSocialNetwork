import { Avatar, Input, Stack, Text, Box, VStack, Textarea, Button, Flex } from '@chakra-ui/react'
import React from 'react'

function PostFeed() {
    return (

        <Box
            bg="white"
            p={6}
            rounded="xl"
            border="1px solid rgba(255,255,255,0.15)"
            w="100%"
        >
            <Stack
                direction="row"
                h="auto"
                w="100%"
                borderBottom={'1px solid var(--feed-div)'}
            >
                <Box
                    display="flex"
                    align="center"
                    alignItems="center"
                    justifyContent="center"
                >
                    <Avatar.Root size={'2xl'} pos={'relative'} >
                        <Avatar.Fallback name="Ime Prezime" />
                        <Avatar.Image src="https://avatars.githubusercontent.com/u/210037477?v=4" />
                    </Avatar.Root>
                </Box>

                <VStack spacing={4} w="100%" >
                    <Box p={4} w="100%">
                        <Input placeholder="Feed title" flex="1" marginBottom={'2'}/>
                        <Textarea
                            placeholder="Whats on your mind..."
                            minHeight='100px'
                            height='100px'
                            maxHeight='200px'
                            w='100%'/>
                        <Flex w="100%" justify="flex-end">
                            <Button
                                variant="outline"
                                style={ {background: 'linear-gradient(45deg, var(--alg-gradient-color-1), var(--alg-gradient-color-2))', color: 'white'} }
                            >
                                Post
                            </Button>
                        </Flex>
                    </Box>
                </VStack>
            </Stack>
        </Box>
    )
}

export default PostFeed