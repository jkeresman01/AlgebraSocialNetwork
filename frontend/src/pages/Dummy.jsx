import React, { useEffect, useState } from 'react'
import axios from 'axios';

const API_BASE = import.meta.env.VITE_ALGEBRA_SOCIAL_NETWORK_BASE_URL;

const getAuthConfig = () => ({
    headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`
    }
});

async function Fetch(params) {
    try {
        return await axios.get(`${API_BASE}/api/v1/users`, getAuthConfig());
    } catch {
        console.error(`Error getting all users from API: ${API_BASE}`);
    }
}

function FetchData() {

  const [data, setData] = useState(null); // Ovdje spremam podatke koje sam dohvatio
  const [error, setError] = useState(null); // Handleanje errora

  /*useEffect(() => {
    axios
    .get(`${API_BASE}/users`, getAuthConfig())
    .then((response) => {
      setData(response.data.content);
      //console.log(response.data.content);
      console.log(data);
    })
    .catch((error) => {
      console.log(error);
    });
  }, []);*/

  useEffect(() => {
    axios.get(`${API_BASE}/api/v1/users`, getAuthConfig())
      .then(response => {

        const result = response.data.content;

        console.log('Dohvaćeni podaci:', result);
        setData(result); // ako dobiješ listu direktno
      })
      .catch(error => {
        console.error('Greška pri dohvaćanju podataka:', error);
        setData([]); // fallback u slučaju greške
      });
  }, []);

  if(error) return <div>{error}</div>;

  return(
    <div className="novo" style={ {padding: 10} }><h2><b>Korisnici (lista):</b></h2>
        <ul>
            {data && data.map(item => (
            <li key={item.id}>
                {item.firstName} ({item.lastName} godina)
            </li>
            ))}
        </ul>
    </div>
  )
}

export const postPost = async (data) => {
    try {
        return await axios.post(`${API_BASE}/api/v1/posts`, data);
    } catch (e) {
        console.log('[POST ERROR] ', e);
        return e;
    }
};

function Dummy() {
    //FetchData();

    const handlePost = async () => {
      try {
          const response = await postPost({
              title: 'Moj prvi post!!!',
              content: 'The European languages are members of the same family. Their separate existence is a myth. For science, music, sport, etc, Europe uses the same vocabulary. The languages only differ in their grammar, their pronunciation and their most common words. Everyone realizes why a new common language would be desirable: one could refuse to pay expensive translators. To achieve this, it would be necessary to have uniform grammar, pronunciation and more common words. If several languages coalesce, the grammar of the resulting language is more simple and regular than that of the individual languages.',
              imageId: null
          });

          /*if (response?.status === 200) {
              login(response.data); // response.data.token
          } else {
              alert("Login failed: " + response?.response?.data?.message || "Unknown error");
          }*/

      } catch (err) {
          console.error('[POST ERROR #2] ', err);
      }
  };

  handlePost();
}

export default Dummy