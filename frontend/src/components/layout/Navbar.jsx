import {
  Box,
  Flex,
  Container,
  HStack,
  IconButton,
  Image,
  useDisclosure,
  Button
} from '@chakra-ui/react';
import { AiOutlineMenu, AiOutlineClose } from 'react-icons/ai';
import { useNavigate } from 'react-router-dom';
import { useContext } from 'react';
import AuthContext from "../../context/AuthContext.jsx";

function Navbar() {
  const { isOpen, onOpen, onClose } = useDisclosure();
  const navigate = useNavigate();
  const { logout } = useContext(AuthContext);

  const navLinks = [
    { label: 'Home', path: '/home' },
    { label: 'Profile', path: '/profile' },
    { label: 'Students', path: '/students' },
    { label: 'Requests', path: '/requests' },
    { label: 'Logout', action: 'logout' },
  ];

  const handleNav = (link) => {
    if (link.label === 'Logout') {
      logout();
    } else {
      navigate(link.path);
    }
    onClose();
  };

  return (
      <>
        <Box bg="var(--topbar-bg)" borderBottom="2px solid black" px={4} color="white">
          <Container maxW="container.xl">
            <Flex h="60px" alignItems="center" justifyContent="space-between">
              <Image
                  src="./alg-pra-logo-white.png"
                  alt="Logo"
                  height="40px"
                  onClick={() => navigate('/')}
                  cursor="pointer"
              />

              <IconButton
                  size="md"
                  icon={isOpen ? <AiOutlineClose /> : <AiOutlineMenu />}
                  display={{ md: 'none' }}
                  onClick={isOpen ? onClose : onOpen}
                  aria-label="Toggle Menu"
                  color="white"
                  bg="transparent"
                  _hover={{ bg: 'gray.700' }}
              />

              <HStack spacing={4} display={{ base: 'none', md: 'flex' }}>
                {navLinks.map((link) => (
                    <Button
                        key={link.label}
                        variant="ghost"
                        style={{ color: 'white' }}
                        _hover={{ bg: 'gray.700' }}
                        onClick={() => handleNav(link)}
                    >
                      {link.label}
                    </Button>
                ))}
              </HStack>
            </Flex>
          </Container>
        </Box>
      </>
  );
}

export default Navbar;
