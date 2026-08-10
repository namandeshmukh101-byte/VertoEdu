# Build stage
FROM node:22-alpine as build
WORKDIR /app

# Install dependencies
COPY package*.json ./
RUN npm ci

# Copy source and build
COPY . .
# We don't need to pass VITE_API_BASE_URL here if Railway injects it at build time. 
# Railway exposes env vars during Docker build automatically.
RUN npm run build

# Serve stage
FROM nginx:alpine
# Copy custom nginx config
COPY nginx.conf /etc/nginx/conf.d/default.conf
# Copy built static files
COPY --from=build /app/dist /usr/share/nginx/html

# Expose port 80
EXPOSE 80

# Start nginx
CMD ["nginx", "-g", "daemon off;"]
