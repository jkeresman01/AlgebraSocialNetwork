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
