import React, { useEffect, useState, useContext } from 'react';
import { Container, Button, Input, Card, Image, Field, Flex, InputGroup } from "@chakra-ui/react";
import { PasswordInput } from "../components/common/PasswordInput.jsx";
import { NavLink } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import AlgBG from '../assets/alg_wd_blur.svg';
import { login as loginRequest } from "../services/authService.js";
import AuthContext from "../context/AuthContext.jsx";

function LoginPage() {
    const { login } = useContext(AuthContext);
    const navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    useEffect(() => {
        document.body.style.background = `url(${AlgBG})`;
        document.body.style.backgroundSize = 'cover';
        document.body.style.backgroundPosition = 'center';
        return () => { document.body.style.background = ''; };
    }, []);

    const handleLogin = async () => {
        try {
            const response = await loginRequest({
                email: email + '@algebra.hr',
                password
            });

            if (response?.status === 200) {
                login(response.data.token);
            } else {
                alert("Login failed: " + response?.data?.message || "Unknown error");
            }

        } catch (err) {
            console.error('Login error:', err);
            alert("Login failed due to server error.");
        }
    };

    return (
        <Container>
            <Flex height="100vh" justifyContent="center" alignItems="center" direction="column" padding="40px">
                <Card.Root lg={{ width: "420px" }}>
                    <Card.Body gap="2">
                        <Image src="https://student.algebra.hr/pretinac/img/main/logo-algebra-black.svg" />
                        <Card.Title mt="2">Login</Card.Title>
                        <hr />
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
                            <PasswordInput value={password} onChange={(e) => setPassword(e.target.value)} />
                        </Field.Root>
                    </Card.Body>
                    <Card.Footer justifyContent="flex-end">
                        <NavLink to="/register">
                            <Button variant="outline">Register</Button>
                        </NavLink>
                        <Button
                            variant="outline"
                            style={{
                                background: 'linear-gradient(45deg, var(--alg-gradient-color-1), var(--alg-gradient-color-2))',
                                color: 'white'
                            }}
                            onClick={handleLogin}
                        >
                            Login
                        </Button>
                    </Card.Footer>
                </Card.Root>
            </Flex>
        </Container>
    );
}

export default LoginPage;
