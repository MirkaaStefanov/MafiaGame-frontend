   const gameId = [[${gameId}]]; // Thymeleaf renders the game ID here
        const token = [[${token}]];  // Thymeleaf renders the token here

        // Ensure the required variables are available
        if (!gameId || !token) {
            console.error("Game ID or token is missing! Ensure Thymeleaf is rendering these variables correctly.");
        }

        // Construct the API URL for fetching players
        const url = `/game/players?gameId=${gameId}`;

        // Function to fetch players from the backend
        function fetchPlayers() {
            console.log(`Fetching players for gameId: ${gameId}`);
            fetch(url, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`, // Pass the token in the Authorization header
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                console.log(`Received response with status: ${response.status}`);
                if (!response.ok) {
                    throw new Error(`Error fetching players: HTTP ${response.status}`);
                }
                return response.json(); // Parse JSON response
            })
            .then(players => {
                console.log("Players fetched successfully:", players);
                updatePlayerList(players); // Update the player list in the UI
            })
            .catch(error => {
                console.error("Error fetching players:", error);
            });
        }

        // Function to update the player list in the UI
        function updatePlayerList(players) {
            console.log("Updating player list with players:", players);
            const playerListContainer = document.getElementById('player-list');

            if (!playerListContainer) {
                console.error("Player list container not found in the DOM.");
                return;
            }

            // Clear the existing list
            playerListContainer.innerHTML = '';

            // Populate the list with fetched player data
            players.forEach(player => {
                const listItem = document.createElement('li');
                listItem.textContent = player.username; // Use the "username" field from the backend
                playerListContainer.appendChild(listItem);
            });
        }

        // Poll the server every 1 second to refresh the player list
        setInterval(fetchPlayers, 1000);


        fetchPlayers();