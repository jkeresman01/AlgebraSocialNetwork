import React, { useEffect, useState } from "react";
import {
  Flex,
  Box,
  Image,
  Grid,
  GridItem,
  Text,
  Field,
  Button,
  Heading,
  Separator,
  Card,
  Container,
  Input,
  Select,
  createListCollection,
  Portal,
  InputGroup,
} from "@chakra-ui/react";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/layout/Navbar";
import { PasswordInput } from "../components/common/PasswordInput";
import {
  getEmailPrefix,
  getFirstName,
  getFullName,
  getGender,
  getLastName,
  getUID,
} from "../utils/utils";
import { updateUser } from "../services/usersService";

function EditProfile() {
  const navigate = useNavigate();

  const [firstName, setFirstName] = useState(getFirstName());
  const [lastName, setLastName] = useState(getLastName());
  const [gender, setGender] = useState("");
  const [email, setEmail] = useState(getEmailPrefix());
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const fullName = getFullName();
  const UID = getUID();

  const genderOptions = createListCollection({
    items: [
      { label: "FEMALE", value: "FEMALE" },
      { label: "MALE", value: "MALE" },
      { label: "OTHER", value: "OTHER" },
    ],
  });

  useEffect(() => {
    let tempGenderFetch = getGender();
    setGender(tempGenderFetch);
  }, []);

  async function handleSubmit(e) {
    e.preventDefault();

    if (password !== confirmPassword) {
      alert("Passwords do not match.");
      return;
    }

    const userData = {
      email,
      password,
      firstName,
      lastName,
      gender,
    };

    try {
      const response = await updateUser(UID, userData);

      if (response?.status === 200) {
        alert("Profile updated successfully!");
        //navigate('/');
      } else {
        alert("Failed to update profile.");
      }
    } catch (error) {
      console.error("Update failed:", error);
    }

    console.log(
      firstName +
        " | " +
        lastName +
        " | " +
        gender +
        " | " +
        email +
        " | " +
        password +
        " | " +
        confirmPassword,
    );
  }

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
              </GridItem>
            </Grid>

            {/* TU DODAJEM EDIT */}
            <form onSubmit={handleSubmit}>
              <Card.Root>
                <Card.Body gap="2">
                  <Card.Title mt="2">Edit your profile</Card.Title>
                  <hr />

                  <Field.Root>
                    <Field.Label>First name</Field.Label>
                    <Input
                      value={firstName}
                      onChange={(e) => setFirstName(e.target.value)}
                    />
                  </Field.Root>

                  <Field.Root>
                    <Field.Label>Last name</Field.Label>
                    <Input
                      value={lastName}
                      onChange={(e) => setLastName(e.target.value)}
                    />
                  </Field.Root>

                  <Select.Root
                    collection={genderOptions}
                    onValueChange={(key) => {
                      setGender(key.value[0]);
                    }}
                  >
                    <Select.HiddenSelect />
                    <Select.Label>Gender</Select.Label>
                    <Select.Control>
                      <Select.Trigger>
                        <Select.ValueText placeholder="Select gender" />
                      </Select.Trigger>
                      <Select.IndicatorGroup>
                        <Select.Indicator />
                      </Select.IndicatorGroup>
                    </Select.Control>
                    <Portal>
                      <Select.Positioner>
                        <Select.Content>
                          {genderOptions.items.map((item) => (
                            <Select.Item item={item} key={item.value}>
                              {item.label}
                              <Select.ItemIndicator />
                            </Select.Item>
                          ))}
                        </Select.Content>
                      </Select.Positioner>
                    </Portal>
                  </Select.Root>

                  <Field.Root>
                    <Field.Label>Email</Field.Label>
                    <InputGroup endAddon="@algebra.hr">
                      <Input
                        placeholder="Enter your email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                      />
                    </InputGroup>
                  </Field.Root>

                  <Field.Root>
                    <Field.Label>Password</Field.Label>
                    <PasswordInput
                      value={password}
                      onChange={(e) => setPassword(e.target.value)}
                    />
                  </Field.Root>

                  <Field.Root>
                    <Field.Label>Confirm password</Field.Label>
                    <PasswordInput
                      value={confirmPassword}
                      onChange={(e) => setConfirmPassword(e.target.value)}
                    />
                  </Field.Root>
                </Card.Body>

                <Card.Footer justifyContent="flex-end">
                  <Button
                    variant="outline"
                    type="submit"
                    style={{
                      background:
                        "linear-gradient(45deg, var(--alg-gradient-color-1), var(--alg-gradient-color-2))",
                      color: "white",
                    }}
                  >
                    Submit change
                  </Button>
                </Card.Footer>
              </Card.Root>
            </form>
          </Box>
        </Flex>
      </Flex>
    </>
  );
}

export default EditProfile;
