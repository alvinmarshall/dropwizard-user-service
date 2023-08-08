import http from 'k6/http';
import {sleep} from 'k6';

const usersToRegisterJson = JSON.parse(open("./data.json"));

const BASE_URL = 'http://user-service:8080';

const registeredUsers = usersToRegisterJson
export let options = {
    scenarios: {
        registration: {
            executor: 'ramping-vus',
            exec: 'registerUser',
            startVUs: 0,
            stages: [
                {duration: '1m', target: 20},
                {duration: '2m', target: 10},
                {duration: '1m', target: 0}
            ],
        },
        getUserByID: {
            executor: 'ramping-vus',
            exec: 'getUserById',
            startVUs: 0,
            startTime: '1m',
            stages: [
                {duration: '1m', target: 20},
                {duration: '2m', target: 10},
                {duration: '1m', target: 0}
            ],
        },
        getAllUsers: {
            executor: 'ramping-vus',
            exec: 'getUsers',
            startVUs: 0,
            stages: [
                {duration: '1m', target: 20},
                {duration: '2m', target: 10},
                {duration: '1m', target: 0}
            ],
        },

    },
};

export function registerUser() {
    for (let user of registeredUsers) {
        let payload = user;
        let headers = {'Content-Type': 'application/json'};
        http.post(`${BASE_URL}/users`, JSON.stringify(payload), {headers});
        sleep(1);
    }
}

export function getUserById() {
    const userId = Math.floor(Math.random() * registeredUsers.length);
    http.get(`${BASE_URL}/users/${userId}`);
    sleep(1);
}


export function getUsers() {
    http.get(`${BASE_URL}/users`);
    sleep(1)
}
