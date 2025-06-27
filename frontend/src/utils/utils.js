export const getUserData = () => {
  const storedData = localStorage.getItem("userData");
  try {
    return storedData ? JSON.parse(storedData) : null;
  } catch (e) {
    console.error("Corrupted JSON in localstorage:", e);
    return null;
  }
};

export const getFullName = () => {
  const user = getUserData();
  return user.firstName + " " + user.lastName;
};

export const getUID = () => {
  return getUserData().id;
};

export const getFirstName = () => {
  return getUserData().firstName;
};

export const getLastName = () => {
  return getUserData().lastName;
};

export const getEmail = () => {
  return getUserData().email;
};

export const getEmailPrefix = () => {
  return getUserData().email.split("@")[0];
};

export const profileImageId = () => {
  return getUserData().profileImageId;
};

export const getGender = () => {
  return getUserData().gender;
};

export const formatDate = (date) => {
  const tempDate = new Date(date);
  return tempDate.toLocaleString("hr-HR", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
};
