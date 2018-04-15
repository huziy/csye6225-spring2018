<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
    <title>User Index</title>
    <script>
        var imageExtentions = ["jpg", "png", "jpeg"];
        function checkFileType() {
            var flag = imageExtentions.indexOf(document.getElementById("profilepicture").value.split(".").pop());
            if (flag == -1) {
                alert("Invalid picture format!");
                return false;
            }
            return true;
        }

    </script>
</head>
<body >
    <div>
        <h1>Hello ${user}</h1>
        <h6 >${currentTime}</h6>
        <img ><img width="200" height="200" alt="No Picture" src=data:image;base64 th:value="${userProfile}" ></h6>

        th:action="@{/users/{action}(action=${action})}"
        <form th:action="@{/user/uploadpicture}" method="POST">
            <input  type="file" id="profilepicture" name="profilepicture" >
            <button type="submit" onclick="return checkFileType()">Upload</button>
        </form>
        <button  type="submit" onclick="window.location.href='/user/deletepicture'">Delete</button>
        <form action="/user/uploadaboutme" method="POST">
            <input  type="text" id="aboutme" name="aboutme" maxlength="140" th:value="${aboutMe}" >
            <button  type="submit" onclick="window.location.href='/user/uploadaboutme'">Upload</button>
        </form>
        <button  type="submit" onclick="window.location.href='/user/logout'">logout</button>
        <p >&copy;CSYE6225</p>
    </div>
</body>
</html>