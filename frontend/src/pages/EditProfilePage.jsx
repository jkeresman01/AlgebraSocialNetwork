import React, { useState } from "react";
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
  Card,
  Input,
  InputGroup,
} from "@chakra-ui/react";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/layout/Navbar";
import {
  getEmailPrefix,
  getFirstName,
  getFullName,
  getLastName,
  getUID,
} from "../utils/utils";
import { updateUser, uploadProfileImage } from "../services/usersService";

function EditProfile() {
  const navigate = useNavigate();

  const [firstName, setFirstName] = useState(getFirstName());
  const [lastName, setLastName] = useState(getLastName());
  const [email, setEmail] = useState(getEmailPrefix());
  const [selectedProfileImage, setSelectedProfileImage] = useState(null);

  const fullName = getFullName();
  const UID = getUID();

  async function handleSubmit(e) {
    e.preventDefault();

    if (selectedProfileImage) {
      const formData = new FormData();
      formData.append("file", selectedProfileImage);

      try {
        await uploadProfileImage(UID, formData);
      } catch (err) {
        console.error("Image upload failed", err);
        alert("Failed to upload profile image");
        return;
      }
    }

    const userData = {
      userId: UID,
      email: email + "@algebra.hr",
      firstName,
      lastName,
    };

    try {
      const response = await updateUser(UID, userData);
      if (response?.status === 200) {
        alert("Profile updated successfully!");
      } else {
        alert("Failed to update profile.");
      }
    } catch (error) {
      console.error("Update failed:", error);
    }
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
                <Heading fontSize="xl" fontFamily="body">
                  {fullName}
                </Heading>
                <Text fontWeight={600} color="gray.500" mb={4}>
                  Programsko inženjerstvo
                </Text>
              </GridItem>
            </Grid>

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
                    <Field.Label>Profile Image</Field.Label>
                    <input
                      type="file"
                      accept=".jpg,.jpeg,.png"
                      onChange={(e) => {
                        if (e.target.files?.[0]) {
                          setSelectedProfileImage(e.target.files[0]);
                        }
                      }}
                    />
                    {selectedProfileImage && (
                      <Box mt={3}>
                        <img
                          src={URL.createObjectURL(selectedProfileImage)}
                          alt="Preview"
                          style={{
                            maxHeight: "150px",
                            borderRadius: "8px",
                          }}
                        />
                      </Box>
                    )}
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
