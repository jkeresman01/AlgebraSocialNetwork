import React from "react";
import { useNavigate } from "react-router-dom";
import {
  Heading,
  Avatar,
  Box,
  Center,
  Text,
  Stack,
  Button,
  Badge,
} from "@chakra-ui/react";
import { getFullName } from "../../utils/utils";

function SideUser() {
  const navigate = useNavigate();
  const fullName = getFullName();

  const handleGoToProfile = () => {
    navigate("/profile");
  };

  return (
    <Center>
      <Box
        maxW={"420px"}
        w={"full"}
        bg="white"
        boxShadow={"2xl"}
        rounded={"lg"}
        p={6}
        textAlign={"center"}
      >
        <Avatar.Root
          size={"2xl"}
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
          <Avatar.Fallback name={fullName} />
          <Avatar.Image src="https://avatars.githubusercontent.com/u/210037477?v=4" />
        </Avatar.Root>
        <Heading
          fontWeight="bold"
          bg="linear-gradient(45deg, var(--alg-gradient-color-1), var(--alg-gradient-color-2))"
          bgClip="text"
          color="transparent"
          fontSize={"xl"}
          fontFamily={"body"}
        >
          {fullName}
        </Heading>
        <Text fontWeight={600} color={"gray.500"} mb={4}>
          Programsko inženjerstvo
        </Text>

        <hr style={{ padding: "5px" }}></hr>

        <Stack direction={"row"} justify={"center"} spacing={4}>
          <Stack spacing={0} align={"center"}>
            <Text fontWeight={600}>200</Text>
            <Text fontSize={"sm"} color={"gray.500"}>
              Following
            </Text>
          </Stack>
          <Stack spacing={0} align={"center"}>
            <Text fontWeight={600}>164</Text>
            <Text fontSize={"sm"} color={"gray.500"}>
              Followers
            </Text>
          </Stack>
        </Stack>

        <Stack align={"center"} justify={"center"} direction={"row"} mt={6}>
          <Badge px={2} py={1} bg={"gray.50"} fontWeight={"400"}>
            #gaming
          </Badge>
          <Badge px={2} py={1} bg={"gray.50"} fontWeight={"400"}>
            #study
          </Badge>
          <Badge px={2} py={1} bg={"gray.50"} fontWeight={"400"}>
            #development
          </Badge>
        </Stack>

        <Stack align={"center"} justify={"center"} direction={"row"} mt={6}>
          <Button
            flex={1}
            fontSize={"sm"}
            rounded={"full"}
            style={{
              background:
                "linear-gradient(45deg, var(--alg-gradient-color-1), var(--alg-gradient-color-2))",
              color: "white",
            }}
            boxShadow={
              "0px 1px 25px -5px rgb(66 153 225 / 48%), 0 10px 10px -5px rgb(66 153 225 / 43%)"
            }
            _hover={{
              bg: "blue.500",
            }}
            _focus={{
              bg: "blue.500",
            }}
            onClick={handleGoToProfile}
          >
            Profile
          </Button>
        </Stack>
      </Box>
    </Center>
  );
}

export default SideUser;
