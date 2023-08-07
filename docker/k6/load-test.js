import http from 'k6/http';
import {group, sleep} from 'k6';

const usersToRegisterJson = JSON.parse(open("./data.json"));

// export let options = {
//     stages: [
//         {duration: '1m', target: 10},   // Ramp up to 10 virtual users over 1 minute
//         {duration: '2m', target: 10},   // Stay at 10 virtual users for 3 minutes
//         {duration: '1m', target: 0},    // Ramp down to 0 virtual users over 1 minute
//     ],
// };

export let options = {
    scenarios: {
        registration: {
            executor: 'constant-arrival-rate',
            rate: 5,  // Requests per second
            duration: '1m',
            preAllocatedVUs: 10,
            maxVUs: 20,
        },
        getAllUsers: {
            executor: 'ramping-vus',
            startVUs: 0,
            stages: [
                {duration: '1m', target: 20},
                // Add more stages as needed
            ],
        },
        getUserByID: {
            executor: 'ramping-vus',
            startVUs: 0,
            stages: [
                {duration: '1m', target: 10},
                // Add more stages as needed
            ],
        },
    },
};


const BASE_URL = 'http://user-service:8080';

// Keep track of registered emails to avoid duplicates
let registeredEmails = new Set();
let usersIds = [];
let registeredUsers = usersToRegisterJson

export default function () {

    if (__ITER === 0) {
        group('Scenario: Registration', () => {
            for (let user of registeredUsers) {
                // Check if the email is not already registered
                if (!registeredEmails.has(user.email)) {
                    // Endpoint 1: Register User
                    let payload = user;
                    let headers = {'Content-Type': 'application/json'};
                    let response = http.post(`${BASE_URL}/users`, JSON.stringify(payload), {headers});

                    // If registration was successful, add the email to the registeredEmails set
                    if (response.status === 200) {
                        registeredEmails.add(user.email);
                        usersIds.push(response.body.data.id);
                    }
                }

                sleep(1);
            }
        });
    }

    if (__ITER === 1) {
        group('Scenario: Get All Users', () => {
            http.get(`${BASE_URL}/users`);
        });
    }

    if (__ITER === 2) {
        group('Scenario: Get User by ID', () => {
            let randomIndex = Math.floor(Math.random() * usersIds.length);
            let userId = usersIds[randomIndex];
            http.get(`${BASE_URL}/users/${userId}`);
        });
    }

    // Other scenarios and load test logic

    sleep(1);  // Add a short delay between iterations
}
