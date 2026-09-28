// ==================================================
// JOBCONNECT - MAIN JAVASCRIPT
// ==================================================

document.addEventListener("DOMContentLoaded", function () {


    // ==================================================
    // AUTHENTICATION / LOGIN
    // ==================================================

    const loginForm =
        document.getElementById("loginForm");

    if (loginForm) {

        loginForm.addEventListener(
            "submit",
            async function (event) {

                event.preventDefault();

                const username =
                    document.getElementById("username")
                    .value
                    .trim();

                const password =
                    document.getElementById("password")
                    .value
                    .trim();

                try {

                    const response =
                        await fetch("/auth/login", {

                            method: "POST",

                            headers: {
                                "Content-Type":
                                    "application/json"
                            },

                            body: JSON.stringify({
                                username: username,
                                password: password
                            })
                        });


                    const data =
                        await response.json();


                    if (!response.ok) {

                        alert(
                            data.message ||
                            "Login failed."
                        );

                        return;
                    }


                    // SAVE LOGIN INFORMATION

                    localStorage.setItem(
                        "token",
                        data.token
                    );

                    localStorage.setItem(
                        "username",
                        data.username
                    );

                    localStorage.setItem(
                        "role",
                        data.role
                    );


                    alert(
                        "Login successful!"
                    );


                    // REDIRECT BASED ON ROLE

                    if (
                        data.role ===
                        "JOB_SEEKER"
                    ) {

                        window.location.href =
                            "/job-seeker-dashboard.html";

                    } else if (
                        data.role ===
                        "EMPLOYER"
                    ) {

                        window.location.href =
                            "/employer-dashboard.html";

                    } else if (
                        data.role ===
                        "ADMIN"
                    ) {

                        window.location.href =
                            "/admin-dashboard.html";

                    } else {

                        window.location.href =
                            "/index.html";
                    }


                } catch (error) {

                    console.error(
                        "Login error:",
                        error
                    );

                    alert(
                        "Unable to connect to the server."
                    );
                }
            }
        );
    }
	// ==================================================
	// JOB SEEKER REGISTRATION
	// ==================================================

	const registerForm = document.getElementById("registerForm");

	if (registerForm) {

	    registerForm.addEventListener("submit", async function (event) {

	        event.preventDefault();

	        const fullName =
	            document.getElementById("fullName").value.trim();

	        const username =
	            document.getElementById("username").value.trim();

	        const email =
	            document.getElementById("email").value.trim();

	        const phone =
	            document.getElementById("phone").value.trim();

	        const password =
	            document.getElementById("password").value;

	        const confirmPassword =
	            document.getElementById("confirmPassword").value;

	        const registerMessage =
	            document.getElementById("registerMessage");


	        // ==================================================
	        // VALIDATION
	        // ==================================================

	        if (
	            !fullName ||
	            !username ||
	            !email ||
	            !phone ||
	            !password ||
	            !confirmPassword
	        ) {

	            registerMessage.textContent =
	                "Please fill in all fields.";

	            return;
	        }


	        if (password !== confirmPassword) {

	            registerMessage.textContent =
	                "Passwords do not match.";

	            return;
	        }


	        if (password.length < 6) {

	            registerMessage.textContent =
	                "Password must be at least 6 characters.";

	            return;
	        }


	        // ==================================================
	        // SEND REGISTRATION REQUEST
	        // ==================================================

	        try {

	            registerMessage.textContent =
	                "Creating your account...";


	            const response = await fetch("/users/register", {

	                method: "POST",

	                headers: {
	                    "Content-Type": "application/json"
	                },

	                body: JSON.stringify({

	                    fullName: fullName,
	                    username: username,
	                    email: email,
	                    phone: phone,
	                    password: password

	                })

	            });


	            // ==================================================
	            // GET RESPONSE
	            // ==================================================

	            const data = await response.json()
	                .catch(() => null);


	            if (!response.ok) {

	                registerMessage.textContent =
	                    typeof data === "string"
	                        ? data
	                        : "Registration failed.";

	                return;
	            }


	            // ==================================================
	            // SUCCESS
	            // ==================================================

	            registerMessage.textContent =
	                "Registration successful! Redirecting to login...";


	            registerForm.reset();


	            setTimeout(function () {

	                window.location.href = "login.html";

	            }, 1500);


	        } catch (error) {

	            console.error(
	                "Registration error:",
	                error
	            );

	            registerMessage.textContent =
	                "Unable to connect to the server.";

	        }

	    });

	}

    // ==================================================
    // NAVIGATION - LOGIN / LOGOUT STATE
    // ==================================================

    const username =
        localStorage.getItem("username");

    const role =
        localStorage.getItem("role");

    const token =
        localStorage.getItem("token");


    const welcomeUser =
        document.getElementById(
            "welcomeUser"
        );

    const profileLink =
        document.getElementById(
            "profileLink"
        );

    const applicationsLink =
        document.getElementById(
            "applicationsLink"
        );

    const managementApplicationsLink =
        document.getElementById(
            "managementApplicationsLink"
        );

    const logoutLink =
        document.getElementById(
            "logoutLink"
        );

    const loginLink =
        document.getElementById(
            "loginLink"
        );

    const registerLink =
        document.getElementById(
            "registerLink"
        );


    // ==================================================
    // USER IS LOGGED IN
    // ==================================================

    if (token && username) {

        // Welcome message

        if (welcomeUser) {

            welcomeUser.textContent =
                "Welcome, " + username;
        }


        // Profile

        if (profileLink) {

            profileLink.style.display =
                "list-item";
        }


        // Logout

        if (logoutLink) {

            logoutLink.style.display =
                "list-item";
        }


        // Hide Login

        if (loginLink) {

            loginLink.style.display =
                "none";
        }


        // Hide Register

        if (registerLink) {

            registerLink.style.display =
                "none";
        }


        // ==================================================
        // JOB SEEKER
        // ==================================================

        if (role === "JOB_SEEKER") {

            if (applicationsLink) {

                applicationsLink.style.display =
                    "list-item";
            }

            if (managementApplicationsLink) {

                managementApplicationsLink.style.display =
                    "none";
            }
        }


        // ==================================================
        // EMPLOYER
        // ==================================================

        if (role === "EMPLOYER") {

            if (managementApplicationsLink) {

                managementApplicationsLink.style.display =
                    "list-item";
            }

            if (applicationsLink) {

                applicationsLink.style.display =
                    "none";
            }
        }

    }


    // ==================================================
    // USER IS NOT LOGGED IN
    // ==================================================

    else {

        if (welcomeUser) {

            welcomeUser.textContent =
                "";
        }


        if (profileLink) {

            profileLink.style.display =
                "none";
        }


        if (applicationsLink) {

            applicationsLink.style.display =
                "none";
        }


        if (managementApplicationsLink) {

            managementApplicationsLink.style.display =
                "none";
        }


        if (logoutLink) {

            logoutLink.style.display =
                "none";
        }


        if (loginLink) {

            loginLink.style.display =
                "list-item";
        }


        if (registerLink) {

            registerLink.style.display =
                "list-item";
        }
    }



    // ==================================================
    // LOGOUT BUTTON
    // ==================================================

    const logoutBtn =
        document.getElementById(
            "logoutBtn"
        );


    if (logoutBtn) {

        logoutBtn.addEventListener(
            "click",
            function (event) {

                event.preventDefault();


                localStorage.removeItem(
                    "token"
                );

                localStorage.removeItem(
                    "username"
                );

                localStorage.removeItem(
                    "role"
                );


                window.location.href =
                    "/index.html";
            }
        );
    }



    // ==================================================
    // LOAD PROFILE
    // ==================================================

	// ==================================================
	// LOAD PROFILE
	// ==================================================
	async function loadProfile() {

	    const profileUsername =
	        document.getElementById("profileUsername");

	    const profileEmail =
	        document.getElementById("profileEmail");

	    const profilePhone =
	        document.getElementById("profilePhone");

	    const profileRole =
	        document.getElementById("profileRole");


	    // GET JWT TOKEN
	    const token =
	        localStorage.getItem("token");


	    // NO TOKEN
	    if (!token) {

	        if (profileUsername) {
	            profileUsername.textContent =
	                "Not available";
	        }

	        if (profileEmail) {
	            profileEmail.textContent =
	                "Not available";
	        }

	        if (profilePhone) {
	            profilePhone.textContent =
	                "Not available";
	        }

	        if (profileRole) {
	            profileRole.textContent =
	                "Not available";
	        }

	        return;
	    }


	    try {

	        const response =
	            await fetch(
	                "/users/me",
	                {
	                    method: "GET",

	                    headers: {
	                        "Authorization":
	                            "Bearer " + token,

	                        "Content-Type":
	                            "application/json"
	                    }
	                }
	            );


	        // CHECK RESPONSE
	        if (!response.ok) {
	            throw new Error(
	                "Failed to load profile"
	            );
	        }


	        // CONVERT RESPONSE TO JSON
	        const data =
	            await response.json();


	        // USERNAME
	        if (profileUsername) {
	            profileUsername.textContent =
	                data.username ||
	                "Not available";
	        }


	        // EMAIL
	        if (profileEmail) {
	            profileEmail.textContent =
	                data.email ||
	                "Not available";
	        }


	        // PHONE
	        if (profilePhone) {
	            profilePhone.textContent =
	                data.phone ||
	                "Not available";
	        }


	        // ROLE
	        if (profileRole) {

	            if (data.role === "JOB_SEEKER") {

	                profileRole.textContent =
	                    "Job Seeker";

	            } else if (data.role === "EMPLOYER") {

	                profileRole.textContent =
	                    "Employer";

	            } else if (data.role === "ADMIN") {

	                profileRole.textContent =
	                    "Administrator";

	            } else {

	                profileRole.textContent =
	                    data.role ||
	                    "User";
	            }
	        }


	    } catch (error) {

	        console.error(
	            "Profile loading error:",
	            error
	        );


	        if (profileUsername) {
	            profileUsername.textContent =
	                "Unable to load";
	        }

	        if (profileEmail) {
	            profileEmail.textContent =
	                "Unable to load";
	        }

	        if (profilePhone) {
	            profilePhone.textContent =
	                "Unable to load";
	        }

	        if (profileRole) {
	            profileRole.textContent =
	                "Unable to load";
	        }
	    }
	}


	loadProfile();



    // ==================================================
    // LOAD ALL JOBS
    // ==================================================

    async function loadJobs() {

        const jobsList =
            document.getElementById(
                "jobsList"
            );


        if (!jobsList) {
            return;
        }


        try {

            const response =
                await fetch("/jobs");


            if (!response.ok) {

                throw new Error(
                    "Unable to load jobs."
                );
            }


            const jobs =
                await response.json();


            displayJobs(jobs);


        } catch (error) {

            console.error(
                "Jobs error:",
                error
            );


            jobsList.innerHTML =
                "<p>Unable to load jobs.</p>";
        }
    }



    // ==================================================
    // DISPLAY JOBS
    // ==================================================

    function displayJobs(jobs) {

        const jobsList =
            document.getElementById(
                "jobsList"
            );


        if (!jobsList) {
            return;
        }


        jobsList.innerHTML =
            "";


        if (
            !jobs ||
            jobs.length === 0
        ) {

            jobsList.innerHTML =
                '<p id="noJobsFound" style="text-align:center;">No Jobs Found</p>';

            return;
        }


        jobs.forEach(
            function (job) {

                const jobCard =
                    document.createElement(
                        "div"
                    );


                jobCard.className =
                    "job-card";


                jobCard.innerHTML = `

                    <h3>
                        ${job.jobTitle || "Untitled Job"}
                    </h3>

                    <p>
                        <strong>Company:</strong>
                        ${job.company || "Not specified"}
                    </p>

                    <p>
                        <strong>Location:</strong>
                        ${job.location || "Not specified"}
                    </p>

                    <p>
                        <strong>Job Type:</strong>
                        ${job.jobType || "Not specified"}
                    </p>

                    <p>
                        <strong>Salary:</strong>
                        ${job.salary || "Not specified"}
                    </p>

                    <p>
                        ${job.description || ""}
                    </p>

					<button class="view-apply-btn"
					        onclick="handleApplyClick(${job.id})">
					    View / Apply
					</button>
                `;


                jobsList.appendChild(
                    jobCard
                );
            }
        );
    }
	// ==================================================
	// HANDLE VIEW / APPLY BUTTON
	// ==================================================

	// ==================================================
	// HANDLE VIEW / APPLY BUTTON
	// ==================================================

	function handleApplyClick(jobId) {

	    const token = localStorage.getItem("token");
	    const role = localStorage.getItem("role");

	    // Visitor
	    if (!token) {

	        alert("Please login before applying.");

	        window.location.href = "/login.html?role=JOB_SEEKER";

	        return;
	    }

	    // Only Job Seekers can apply
	    if (role !== "JOB_SEEKER") {

	        alert("Only Job Seekers can apply for jobs.");

	        return;
	    }

	    // Job Seeker
	    window.location.href = "/apply.html?id=" + jobId;
	}

	// MAKE FUNCTION AVAILABLE TO INLINE BUTTON
	window.handleApplyClick = handleApplyClick;

    // ==================================================
    // LOAD SELECTED JOB ON APPLY PAGE
    // ==================================================

    async function loadSelectedJob() {

        const selectedJob =
            document.getElementById(
                "selectedJob"
            );


        if (!selectedJob) {
            return;
        }


        const jobId =
            new URLSearchParams(
                window.location.search
            ).get("id");


        if (!jobId) {

            selectedJob.textContent =
                "No job selected";

            return;
        }


        try {

            const response =
                await fetch(
                    "/jobs/" + jobId
                );


            if (!response.ok) {

                throw new Error(
                    "Unable to load job."
                );
            }


            const job =
                await response.json();


            selectedJob.textContent =
                job.jobTitle ||
                "Job title not available";


        } catch (error) {

            console.error(
                "Selected job error:",
                error
            );


            selectedJob.textContent =
                "Unable to load job";
        }
    }



    // ==================================================
    // JOB SEARCH
    // ==================================================

    const searchBtn =
        document.getElementById(
            "searchBtn"
        );

    const jobSearch =
        document.getElementById(
            "jobSearch"
        );


    if (
        searchBtn &&
        jobSearch
    ) {

        searchBtn.addEventListener(
            "click",
            searchJobs
        );


        jobSearch.addEventListener(
            "keyup",
            function (event) {

                if (
                    event.key ===
                    "Enter"
                ) {

                    searchJobs();
                }
            }
        );
    }



    function searchJobs() {

        if (!jobSearch) {
            return;
        }


        const searchValue =
            jobSearch.value
                .toLowerCase()
                .trim();


        const jobCards =
            document.querySelectorAll(
                ".job-card"
            );


        let found =
            false;


        jobCards.forEach(
            function (card) {

                const text =
                    card.textContent
                        .toLowerCase();


                if (
                    searchValue === "" ||
                    text.includes(
                        searchValue
                    )
                ) {

                    card.style.display =
                        "block";

                    found =
                        true;

                } else {

                    card.style.display =
                        "none";
                }
            }
        );


        const noJobsFound =
            document.getElementById(
                "noJobsFound"
            );


        if (noJobsFound) {

            noJobsFound.style.display =
                found
                    ? "none"
                    : "block";
        }
    }



    // ==================================================
    // APPLICATION FORM
    // ==================================================

    const applicationForm =
        document.getElementById(
            "applicationForm"
        );


    if (applicationForm) {

        applicationForm.addEventListener(
            "submit",
            submitApplication
        );
    }



    async function submitApplication(event) {

        event.preventDefault();


		const token = localStorage.getItem("token");
		const role = localStorage.getItem("role");

		// User must be logged in
		if (!token) {

		    alert("Please login before applying.");

		    window.location.href = "/login.html?role=JOB_SEEKER";

		    return;
		}

		// Only Job Seekers can submit applications
		if (role !== "JOB_SEEKER") {

		    alert("Only Job Seekers can apply for jobs.");

		    window.location.href = "/jobs.html";

		    return;
		}

        const jobId =
            new URLSearchParams(
                window.location.search
            ).get("id");


        if (!jobId) {

            alert(
                "No job selected."
            );


            return;
        }


        const fullName =
            document.getElementById(
                "fullName"
            ).value.trim();


        const email =
            document.getElementById(
                "email"
            ).value.trim();


        const phone =
            document.getElementById(
                "phone"
            ).value.trim();


        const coverLetter =
            document.getElementById(
                "coverLetter"
            ).value.trim();


        const selectedJob =
            document.getElementById(
                "selectedJob"
            );


        try {

            // GET JOB DETAILS

            const jobResponse =
                await fetch(
                    "/jobs/" + jobId
                );


            if (!jobResponse.ok) {

                throw new Error(
                    "Unable to load selected job."
                );
            }


            const job =
                await jobResponse.json();


            const jobTitle =
                job.jobTitle ||
                "Unknown Job";


            if (selectedJob) {

                selectedJob.textContent =
                    jobTitle;
            }


            // SUBMIT APPLICATION

            const response =
                await fetch(
                    "/applications",
                    {

                        method: "POST",

                        headers: {

                            "Content-Type":
                                "application/json",

                            "Authorization":
                                "Bearer " + token
                        },

                        body: JSON.stringify({

                            jobId:
                                jobId,

                            jobTitle:
                                jobTitle,

                            fullName:
                                fullName,

                            email:
                                email,

                            phone:
                                phone,

                            coverLetter:
                                coverLetter
                        })
                    }
                );


				if (!response.ok) {

				    const errorText =
				        await response.text();

				    if (response.status === 409) {

				        throw new Error(
				            "You have already applied for this job."
				        );
				    }

				    throw new Error(
				        errorText ||
				         "Application submission failed."
				    );
				}            alert(
                "Application submitted successfully!"
            );


            window.location.href =
                "/my-applications.html";


        } catch (error) {

            console.error(
                "Application error:",
                error
            );


			alert(
			    error.message
			);
        }
    }



	// ==================================================
	// LOAD MY APPLICATIONS
	// ==================================================

	async function loadApplications() {

	    const applicationsList =
	        document.getElementById(
	            "applicationsList"
	        );

	    if (!applicationsList) {
	        return;
	    }

	    const token =
	        localStorage.getItem(
	            "token"
	        );

	    if (!token) {

	        window.location.href =
	            "/login.html";

	        return;
	    }

	    try {

	        const response =
	            await fetch(
	                "/applications/my",
	                {
	                    method: "GET",

	                    headers: {
	                        "Authorization":
	                            "Bearer " + token
	                    }
	                }
	            );

	        if (!response.ok) {

	            throw new Error(
	                "Unable to load applications."
	            );
	        }

	        const applications =
	            await response.json();

	        applicationsList.innerHTML =
	            "";

	        if (
	            !applications ||
	            applications.length === 0
	        ) {

	            applicationsList.innerHTML =
	                "<p>No applications found.</p>";

	            return;
	        }


	        // ==================================================
	        // EDIT APPLICATION
	        // ==================================================

	        function editApplication(id) {

	            window.location.href =
	                "/edit-application.html?id=" + id;
	        }

	        // MAKE FUNCTION AVAILABLE TO INLINE BUTTON

	        window.editApplication =
	            editApplication;


	        // ==================================================
	        // DISPLAY APPLICATIONS
	        // ==================================================

	        applications.forEach(
	            function (application) {

	                const card =
	                    document.createElement(
	                        "div"
	                    );

	                card.className =
	                    "application-card";


	                const status =
	                    application.status ||
	                    "Pending";


	                const statusClass =
	                    status.toLowerCase();


	                // ==================================================
	                // ACTION BUTTONS
	                // ==================================================

	                let actionButton = "";


	                if (
	                    status.toLowerCase() ===
	                    "pending"
	                ) {

	                    actionButton = `

	                        <div class="application-actions">

	                            <button
	                                class="edit-application-btn"
	                                onclick="editApplication(${application.id})">

	                                ✏️ Edit Application

	                            </button>

	                            <button
	                                class="withdraw-application-btn"
	                                onclick="withdrawApplication(${application.id})">

	                                ↩️ Withdraw Application

	                            </button>

	                        </div>

	                    `;

	                } else {

	                    actionButton = `

	                        <div class="application-actions">

	                            <span class="application-locked">

	                                🔒 Application Locked

	                            </span>

	                        </div>

	                    `;
	                }


	                // ==================================================
	                // APPLICATION CARD
	                // ==================================================

	                card.innerHTML = `

	                    <h3>

	                        ${application.jobTitle || "Job"}

	                    </h3>


	                    <p>

	                        <strong>Name:</strong>

	                        ${application.fullName || ""}

	                    </p>


	                    <p>

	                        <strong>Email:</strong>

	                        ${application.email || ""}

	                    </p>


	                    <p>

	                        <strong>Phone:</strong>

	                        ${application.phone || ""}

	                    </p>


	                    <p>

	                        <strong>Cover Letter:</strong>

	                        ${application.coverLetter || "Not provided"}

	                    </p>


	                    <p>

	                        <strong>Status:</strong>

	                        <span
	                            class="application-status ${statusClass}">

	                            ${status}

	                        </span>

	                    </p>


	                    <p>

	                        <strong>Applied:</strong>

	                        ${application.appliedAt || ""}

	                    </p>


	                    ${actionButton}

	                `;


	                applicationsList.appendChild(
	                    card
	                );

	            }
	        );


	    } catch (error) {

	        console.error(
	            "Applications error:",
	            error
	        );

	        applicationsList.innerHTML =
	            "<p>Unable to load applications.</p>";
	    }
	}


	// ==================================================
	// WITHDRAW APPLICATION
	// ==================================================

	async function withdrawApplication(id) {

	    const token =
	        localStorage.getItem(
	            "token"
	        );

	    if (!token) {

	        alert(
	            "Please login first."
	        );

	        return;
	    }


	    // -----------------------------------------
	    // CONFIRM WITHDRAWAL
	    // -----------------------------------------

	    const confirmed =
	        confirm(
	            "Are you sure you want to withdraw this application?"
	        );

	    if (!confirmed) {

	        return;
	    }


	    try {

	        const response =
	            await fetch(
	                "/applications/" +
	                id +
	                "/withdraw",
	                {
	                    method: "DELETE",

	                    headers: {
	                        "Authorization":
	                            "Bearer " + token
	                    }
	                }
	            );


	        if (!response.ok) {

	            const errorText =
	                await response.text();

	            throw new Error(
	                errorText ||
	                "Unable to withdraw application."
	            );
	        }


	        alert(
	            "Application withdrawn successfully."
	        );


	        // Reload applications

	        loadApplications();


	    } catch (error) {

	        console.error(
	            "Withdraw application error:",
	            error
	        );


	        alert(
	            "Unable to withdraw application: " +
	            error.message
	        );
	    }
	}


	// MAKE FUNCTION AVAILABLE TO INLINE BUTTON

	window.withdrawApplication =
	    withdrawApplication;

		// ==================================================
		// LOAD EMPLOYER APPLICATIONS
		// ==================================================

		async function loadMyApplications() {

		    const employerApplications =
		        document.getElementById(
		            "applicationsManagementList"
		        );

		    const loadingMessage =
		        document.getElementById(
		            "managementLoading"
		        );

		    const noApplicationsMessage =
		        document.getElementById(
		            "noManagementApplications"
		        );


		    // Page does not contain employer applications
		    if (!employerApplications) {
		        return;
		    }


		    const token =
		        localStorage.getItem("token");


		    // -----------------------------------------
		    // CHECK LOGIN
		    // -----------------------------------------

		    if (!token) {

		        window.location.href =
		            "/login.html";

		        return;
		    }


		    // -----------------------------------------
		    // SHOW LOADING
		    // -----------------------------------------

		    if (loadingMessage) {

		        loadingMessage.style.display =
		            "block";
		    }


		    if (noApplicationsMessage) {

		        noApplicationsMessage.style.display =
		            "none";
		    }


		    employerApplications.innerHTML = "";


		    try {

		        // -----------------------------------------
		        // GET APPLICATIONS
		        // -----------------------------------------

		        const response =
		            await fetch(
		                "/applications",
		                {
		                    method: "GET",

		                    headers: {
		                        "Authorization":
		                            "Bearer " + token
		                    }
		                }
		            );


		        if (!response.ok) {

		            throw new Error(
		                "Unable to load applications."
		            );
		        }


		        const applications =
		            await response.json();


		        // -----------------------------------------
		        // HIDE LOADING
		        // -----------------------------------------

		        if (loadingMessage) {

		            loadingMessage.style.display =
		                "none";
		        }


		        // -----------------------------------------
		        // NO APPLICATIONS
		        // -----------------------------------------

		        if (
		            !applications ||
		            applications.length === 0
		        ) {

		            if (noApplicationsMessage) {

		                noApplicationsMessage.style.display =
		                    "block";
		            }

		            return;
		        }


		        // -----------------------------------------
		        // DISPLAY APPLICATIONS
		        // -----------------------------------------

		        applications.forEach(
		            function (application) {

		                const card =
		                    document.createElement(
		                        "div"
		                    );


		                card.className =
		                    "application-card";


		                // -----------------------------------------
		                // STATUS
		                // -----------------------------------------

		                const status =
		                    application.status ||
		                    "Pending";


		                const normalizedStatus =
		                    status.toLowerCase();


		                const statusClass =
		                    normalizedStatus;


		                // -----------------------------------------
		                // ACTION BUTTONS
		                // -----------------------------------------

		                let actionButtons = "";


		                if (
		                    normalizedStatus ===
		                    "pending"
		                ) {

		                    actionButtons = `

		                        <div class="application-actions">

		                            <button
		                                type="button"
		                                class="accept-btn"
		                                onclick="acceptApplication(${application.id})">

		                                ✓ Accept

		                            </button>


		                            <button
		                                type="button"
		                                class="reject-btn"
		                                onclick="rejectApplication(${application.id})">

		                                ✕ Reject

		                            </button>

		                        </div>

		                    `;

		                } else if (
		                    normalizedStatus ===
		                    "accepted"
		                ) {

		                    actionButtons = `

		                        <div class="application-actions">

		                            <span class="application-locked">

		                                ✓ Application Accepted

		                            </span>

		                        </div>

		                    `;

		                } else if (
		                    normalizedStatus ===
		                    "rejected"
		                ) {

		                    actionButtons = `

		                        <div class="application-actions">

		                            <span class="application-locked">

		                                Application Rejected

		                            </span>

		                        </div>

		                    `;

		                } else {

		                    actionButtons = `

		                        <div class="application-actions">

		                            <span class="application-locked">

		                                🔒 Application Closed

		                            </span>

		                        </div>

		                    `;
		                }


		                // -----------------------------------------
		                // APPLICATION CARD
		                // -----------------------------------------

		                card.innerHTML = `

		                    <div class="application-card-header">

		                        <div>

		                            <h3>
		                                ${application.jobTitle || "Job"}
		                            </h3>

		                            <p class="application-applicant">

		                                Applicant:
		                                <strong>
		                                    ${application.fullName || "Not provided"}
		                                </strong>

		                            </p>

		                        </div>


		                        <span
		                            class="application-status ${statusClass}">

		                            ${status}

		                        </span>

		                    </div>


		                    <div class="application-card-body">

		                        <p>

		                            <strong>Email:</strong>

		                            ${application.email || "Not provided"}

		                        </p>


		                        <p>

		                            <strong>Phone:</strong>

		                            ${application.phone || "Not provided"}

		                        </p>


		                        <p>

		                            <strong>Cover Letter:</strong>

		                        </p>


		                        <div class="cover-letter">

		                            ${application.coverLetter || "No cover letter provided."}

		                        </div>

		                    </div>


		                    ${actionButtons}

		                `;


		                employerApplications.appendChild(
		                    card
		                );

		            }
		        );


		    } catch (error) {

		        console.error(
		            "Employer applications error:",
		            error
		        );


		        // Hide loading

		        if (loadingMessage) {

		            loadingMessage.style.display =
		                "none";
		        }


		        // Show error

		        employerApplications.innerHTML = `

		            <div class="application-error">

		                <h3>
		                    Unable to Load Applications
		                </h3>

		                <p>
		                    Please refresh the page and try again.
		                </p>

		            </div>

		        `;

		    }

		}


	
	// ==================================================
	// ACCEPT APPLICATION
	// ==================================================
	async function acceptApplication(id) {

	    const token =
	        localStorage.getItem("token");

	    if (!token) {
	        alert("Please login first.");
	        return;
	    }

	    try {

	        const response =
	            await fetch(
	                "/applications/" +
	                id +
	                "/accept",
	                {
	                    method: "PUT",
	                    headers: {
	                        "Authorization":
	                            "Bearer " + token
	                    }
	                }
	            );

	        if (!response.ok) {
	            throw new Error(
	                "Unable to accept application."
	            );
	        }

	        alert(
	            "Application accepted successfully."
	        );

	        loadMyApplications();

	    } catch (error) {

	        console.error(
	            "Accept application error:",
	            error
	        );

	        alert(
	            "Unable to accept application."
	        );
	    }
	}

	// MAKE FUNCTION AVAILABLE TO INLINE BUTTON
	window.acceptApplication = acceptApplication;


	// ==================================================
	// REJECT APPLICATION
	// ==================================================
	async function rejectApplication(id) {

	    const token =
	        localStorage.getItem("token");

	    if (!token) {
	        alert("Please login first.");
	        return;
	    }

	    try {

	        const response =
	            await fetch(
	                "/applications/" +
	                id +
	                "/reject",
	                {
	                    method: "PUT",
	                    headers: {
	                        "Authorization":
	                            "Bearer " + token
	                    }
	                }
	            );

	        if (!response.ok) {
	            throw new Error(
	                "Unable to reject application."
	            );
	        }

	        alert(
	            "Application rejected."
	        );

	        loadMyApplications();

	    } catch (error) {

	        console.error(
	            "Reject application error:",
	            error
	        );

	        alert(
	            "Unable to reject application."
	        );
	    }
	}

	// MAKE FUNCTION AVAILABLE TO INLINE BUTTON
	window.rejectApplication = rejectApplication;


    // ==================================================
    // LOAD EMPLOYER JOBS
    // ==================================================

    async function loadMyJobs() {

        const myJobsList =
            document.getElementById(
                "myJobsList"
            );


        if (!myJobsList) {
            return;
        }


        const token =
            localStorage.getItem(
                "token"
            );


        if (!token) {

            window.location.href =
                "/login.html";

            return;
        }


        try {

            const response =
                await fetch(
                    "/jobs/my",
                    {

                        method: "GET",

                        headers: {

                            "Authorization":
                                "Bearer " + token
                        }
                    }
                );


            if (!response.ok) {

                throw new Error(
                    "Unable to load your jobs."
                );
            }


            const jobs =
                await response.json();


            myJobsList.innerHTML =
                "";


            if (
                !jobs ||
                jobs.length === 0
            ) {

                myJobsList.innerHTML =
                    "<p>You have not posted any jobs yet.</p>";

                return;
            }


            jobs.forEach(
                function (job) {

                    const card =
                        document.createElement(
                            "div"
                        );


                    card.className =
                        "job-card";


						card.innerHTML = `

						    <h3>

						        ${job.jobTitle || ""}

						    </h3>

						    <p>

						        <strong>Company:</strong>

						        ${job.company || ""}

						    </p>

						    <p>

						        <strong>Location:</strong>

						        ${job.location || ""}

						    </p>

						    <p>

						        <strong>Job Type:</strong>

						        ${job.jobType || ""}

						    </p>

						    <p>

						        <strong>Salary:</strong>

						        ${job.salary || ""}

						    </p>

						    <p>

						        ${job.description || ""}

						    </p>

						    <div class="job-card-actions">

						        <a

						            href="edit-job.html?id=${job.id}"

						            class="edit-job-btn">

						            Edit Job

						        </a>

						    </div>

						`;

                    myJobsList.appendChild(
                        card
                    );
                }
            );


        } catch (error) {

            console.error(
                "My jobs error:",
                error
            );


            myJobsList.innerHTML =
                "<p>Unable to load your jobs.</p>";
        }
    }



    // ==================================================
    // CREATE JOB FORM
    // ==================================================

    const jobForm =
        document.getElementById(
            "jobForm"
        );


    if (jobForm) {

        jobForm.addEventListener(
            "submit",
            async function (event) {

                event.preventDefault();


                const token =
                    localStorage.getItem(
                        "token"
                    );


                if (!token) {

                    alert(
                        "Please login first."
                    );


                    window.location.href =
                        "/login.html";


                    return;
                }


                const jobTitle =
                    document.getElementById(
                        "jobTitle"
                    ).value.trim();


                const companyName =
                    document.getElementById(
                        "companyName"
                    ).value.trim();


                const location =
                    document.getElementById(
                        "location"
                    ).value.trim();


                const jobType =
                    document.getElementById(
                        "jobType"
                    ).value.trim();


                const salary =
                    document.getElementById(
                        "salary"
                    ).value.trim();


                const description =
                    document.getElementById(
                        "description"
                    ).value.trim();


                try {

                    const response =
                        await fetch(
                            "/jobs",
                            {

                                method: "POST",

                                headers: {

                                    "Content-Type":
                                        "application/json",

                                    "Authorization":
                                        "Bearer " +
                                        token
                                },

                                body:
                                    JSON.stringify({

                                        jobTitle:
                                            jobTitle,

                                        company:
                                            companyName,

                                        location:
                                            location,

                                        jobType:
                                            jobType,

                                        salary:
                                            salary,

                                        description:
                                            description
                                    })
                            }
                        );


                    if (!response.ok) {

                        const errorText =
                            await response.text();


                        throw new Error(
                            errorText ||
                            "Unable to create job."
                        );
                    }


                    alert(
                        "Job posted successfully!"
                    );


                    jobForm.reset();


                    loadMyJobs();


                } catch (error) {

                    console.error(
                        "Create job error:",
                        error
                    );


                    alert(
                        "Unable to post job: " +
                        error.message
                    );
                }
            }
        );
    }


	// ==================================================
	// LOAD ADMIN DASHBOARD STATISTICS
	// ==================================================

	async function loadAdminStatistics() {

	    const totalUsers =
	        document.getElementById("totalUsers");

	    const totalJobSeekers =
	        document.getElementById("totalJobSeekers");

	    const totalEmployers =
	        document.getElementById("totalEmployers");

	    const totalJobs =
	        document.getElementById("totalJobs");

	    const totalApplications =
	        document.getElementById("totalApplications");

	    const adminUsername =
	        document.getElementById("adminUsername");

	    const adminDashboardMessage =
	        document.getElementById(
	            "adminDashboardMessage"
	        );


	    // This function only runs on the Admin Dashboard

	    if (!totalUsers) {
	        return;
	    }


	    // Get login information

	    const token =
	        localStorage.getItem("token");

	    const username =
	        localStorage.getItem("username");

	    const role =
	        localStorage.getItem("role");


	    // Check login

	    if (!token) {

	        window.location.href =
	            "/login.html";

	        return;
	    }


	    // Check ADMIN role

	    if (role !== "ADMIN") {

	        alert(
	            "Access denied. Admin only."
	        );

	        window.location.href =
	            "/index.html";

	        return;
	    }


	    // Display admin username

	    if (adminUsername) {

	        adminUsername.textContent =
	            username || "admin";
	    }


	    try {

	        const response =
	            await fetch(
	                "/admin/statistics",
	                {
	                    method: "GET",

	                    headers: {
	                        "Authorization":
	                            "Bearer " + token,

	                        "Content-Type":
	                            "application/json"
	                    }
	                }
	            );


	        // Check server response

	        if (!response.ok) {

	            throw new Error(
	                "Unable to load admin statistics."
	            );
	        }


	        // Convert response to JSON

	        const data =
	            await response.json();


	        // Display statistics

	        if (totalUsers) {

	            totalUsers.textContent =
	                data.totalUsers;
	        }


	        if (totalJobSeekers) {

	            totalJobSeekers.textContent =
	                data.totalJobSeekers;
	        }


	        if (totalEmployers) {

	            totalEmployers.textContent =
	                data.totalEmployers;
	        }


	        if (totalJobs) {

	            totalJobs.textContent =
	                data.totalJobs;
	        }


	        if (totalApplications) {

	            totalApplications.textContent =
	                data.totalApplications;
	        }


	        // Clear loading/error message

	        if (adminDashboardMessage) {

	            adminDashboardMessage.textContent =
	                "";
	        }


	    } catch (error) {

	        console.error(
	            "Admin dashboard error:",
	            error
	        );


	        // Show error in dashboard

	        if (adminDashboardMessage) {

	            adminDashboardMessage.textContent =
	                "Unable to load dashboard statistics.";

	        }

	    }

	}
    // ==================================================
    // RUN PAGE-SPECIFIC FUNCTIONS
    // ==================================================

    // JOBS PAGE

    loadJobs();


    // APPLY PAGE

    loadSelectedJob();


    // MY APPLICATIONS PAGE

    loadApplications();


    // EMPLOYER APPLICATION MANAGEMENT

    loadMyApplications();


    // EMPLOYER JOBS

    loadMyJobs();

	// ADMIN DASHBOARD
	loadAdminStatistics();
});