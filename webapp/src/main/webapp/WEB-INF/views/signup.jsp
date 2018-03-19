<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Sign up</title>
</head>
<body >
    <form  action="/user/signupvalidate" method="POST">
        <h1 >Please sign up</h1>
        <input name="username" id="inputEmail"  placeholder="Email address" required="" autofocus="" type="email">
        <input name="password" id="inputPassword"  placeholder="Password" required="" type="password">
        <input type="hidden" name="username" value="" />
        <button type="submit">Sign up</button>
        <p>&copy;CSYE6225</p>
    </form>
</body>
</html>