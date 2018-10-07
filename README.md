# Giphy Sample App

A simple app that pulls gifs from the Giphy API using a random keyword or phrase from a hard-coded list of keywords and
phrases.

The following decisions went into this app:
- Dagger for dependency injection
- Glide for displaying placeholders and GIFs
- Android Paging Library for requesting GIFs in an efficient manner

## Screenshot

![screenshot](screenshots/screenshot.png)

## Before you build

Before building the app, you'll need to get an API key from Giphy and set it as the value of the `GIPHY_API_KEY`
build config field in the app `build.gradle` file. Visit [the docs](https://developers.giphy.com/docs/#access) for
instructions on how to get an API key.

