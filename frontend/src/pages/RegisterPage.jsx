import React, { useEffect, useState } from 'react';
import {
  Container,
  Button,
  Input,
  Card,
  Image,
  Field,
  Flex,
  InputGroup,
  Select,
  createListCollection,
  Portal
} from "@chakra-ui/react";
import { PasswordInput } from "../components/common/PasswordInput";
import { NavLink, useNavigate } from 'react-router-dom';
import AlgBG from '../assets/alg_wd_blur.svg';
import { register } from "../services/authService.js";

const genderOptions = createListCollection({
  items: [
    { label: "FEMALE", value: "FEMALE" },
    { label: "MALE", value: "MALE" },
    { label: "OTHER", value: "OTHER" },
  ],
});

function RegisterPage() {
  const navigate = useNavigate();

  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName]  = useState('');
  const [email, setEmail]        = useState('');
  const [password, setPassword]  = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [gender, setGender]      = useState('');

  useEffect(() => {
    document.body.style.background = `url(${AlgBG})`;
    document.body.style.backgroundSize = 'cover';
    document.body.style.backgroundPosition = 'center';
    return () => { document.body.style.background = ''; };
  }, []);

  const validateForm = () => {
    if (!firstName || !lastName || !email || !password || !confirmPassword || !gender) {
      alert("All fields are required.");
      
      return false;
    }
    if (!email.match(/^[a-zA-Z0-9._%+-]+$/)) {
      alert("Invalid email format (exclude @algebra.hr).");
      return false;
    }
    if (password !== confirmPassword) {
      alert("Passwords do not match.");
      return false;
    }
    return true;
  };

  const handleSubmit = (e) => {
        e.preventDefault();
        handleRegister();
    };

  const handleRegister = async () => {
    if (!validateForm()) return;

    const userData = {
      email: `${email.trim()}@algebra.hr`,
      password,
      firstName,
      lastName,
      gender,
    };

    try {
      const response = await register(userData);
      if (response?.status === 200 || response?.status === 201) {
        alert("Registration successful! You can now log in.");
        navigate('/');
      } else {
        
        alert("Registration failed: " + (response?.response?.data?.message || "Unknown error"));
      }
    } catch (error) {
      alert("Server error: Could not connect to the server.");
    }
  };

  return (
      <Container>
        <Flex height="100vh" justifyContent="center" alignItems="center" direction="column" padding="50px">
          <form onSubmit={handleSubmit}>
          <Card.Root lg={{ width: "420px" }}>
            <Card.Body gap="2">
              <Image style={{ margin: 'auto' }} src="https://student.algebra.hr/pretinac/img/main/logo-algebra-black.svg" />
              <Card.Title mt="2">Register</Card.Title>
              <hr />

              <Field.Root>
                <Field.Label>First name</Field.Label>
                <Input value={firstName} onChange={e => setFirstName(e.target.value)} />
              </Field.Root>

              <Field.Root>
                <Field.Label>Last name</Field.Label>
                <Input value={lastName} onChange={e => setLastName(e.target.value)} />
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
                    <Select.ValueText placeholder="Select gender"/>
                  </Select.Trigger>
                  <Select.IndicatorGroup>
                    <Select.Indicator />
                  </Select.IndicatorGroup>
                </Select.Control>
                <Portal>
                  <Select.Positioner>
                    <Select.Content>
                      {genderOptions.items.map(item => (
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
                      onChange={e => setEmail(e.target.value)}
                  />
                </InputGroup>
              </Field.Root>

              <Field.Root>
                <Field.Label>Password</Field.Label>
                <PasswordInput value={password} onChange={e => setPassword(e.target.value)} />
              </Field.Root>

              <Field.Root>
                <Field.Label>Confirm password</Field.Label>
                <PasswordInput value={confirmPassword} onChange={e => setConfirmPassword(e.target.value)} />
              </Field.Root>
            </Card.Body>

            <Card.Footer justifyContent="flex-end">
              <NavLink to="/" end>
                <Button variant="outline">Back</Button>
              </NavLink>
              <Button variant="outline" type="submit" style={{
                background: 'linear-gradient(45deg, var(--alg-gradient-color-1), var(--alg-gradient-color-2))',
                color: 'white'
              }}>
                Register
              </Button>
            </Card.Footer>
          </Card.Root>
          </form>
        </Flex>
      </Container>
  );
}

export default RegisterPage;
