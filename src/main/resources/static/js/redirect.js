
        const userId = [[${userId}]];

        // Establish WebSocket connection
        const socket = new SockJS('/websocket'); // Connect to the backend WebSocket endpoint
        const stompClient = Stomp.over(socket);


        stompClient.connect({}, function () {
            console.log('Connected to WebSocket server');


            stompClient.subscribe(`/topic/redirect/${userId}`, function (message) {
                const redirectUrl = message.body; // Extract the URL from the message
                console.log(`Redirecting to: ${redirectUrl}`);

                // Perform the redirection
                window.location.href = redirectUrl;
            });
        });

        // Handle disconnects or errors
        stompClient.onDisconnect = function () {
            console.log('Disconnected from WebSocket server');
        };

        stompClient.onStompError = function (frame) {
            console.error('WebSocket error:', frame.headers['message']);
        };