# pull the official base image
FROM node:17.9.0-alpine

# set working direction
WORKDIR /frontend

# add `/app/node_modules/.bin` to $PATH
ENV PATH="./node_modules/.bin:$PATH"

# install application dependencies
COPY package.json ./
COPY package-lock.json ./
RUN npm i

# add app
COPY . ./

# start app
CMD ["npm", "start"]