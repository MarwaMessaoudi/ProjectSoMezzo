import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

// Password strength validation helper function
const checkPasswordStrength = (password) => {
  const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@#$%^&+=!])[\w@#$%^&+=!]{8,}$/;
  return passwordRegex.test(password);
};

function AddForm() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    first_name: "",
    last_name: "",
    email: "",
    birthDate: "",
    role: "",
    password: "",
    confirmPassword: "",
  });

  const [errors, setErrors] = useState({});
  const [isSubmitting, setIsSubmitting] = useState(false); // To manage loading state
  const [passwordStrength, setPasswordStrength] = useState(""); // To show password strength feedback

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });

    // Check password strength if password is being typed
    if (name === "password") {
      if (checkPasswordStrength(value)) {
        setPasswordStrength("Strong");
      } else {
        setPasswordStrength("Weak");
      }
    }
  };

  const validateForm = () => {
    const newErrors = {};

    if (!formData.first_name) newErrors.first_name = "First Name required";
    if (!formData.last_name) newErrors.last_name = "Last Name required";
    if (!formData.email) newErrors.email = "Email required";
    if (!formData.birthDate) newErrors.birthDate = "Birth Date required";
    if (!formData.role) newErrors.role = "Position required";

    if (!formData.password) {
      newErrors.password = "Password required";
    } else if (!checkPasswordStrength(formData.password)) {
      newErrors.password =
        "Password must include at least 8 characters, with uppercase, lowercase, a number, and a special character (@, #, $, %, ^, &, +, =, or !)";
    }

    if (formData.password !== formData.confirmPassword) {
      newErrors.confirmPassword = "Passwords do not match";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (validateForm()) {
      setIsSubmitting(true); // Set submitting to true when request starts

      const token =
      localStorage.getItem("accessToken") || sessionStorage.getItem("accessToken");
    
    if (!token) {
      alert("No access token found. Please log in again.");
      navigate("/sign-in");
      return;
    }
    

      
      try {
        const response = await axios.post(
          "http://localhost:8084/Users/addWithApproval",
          {
            first_name: formData.first_name,
            last_name: formData.last_name,
            email: formData.email,
            birthDate: formData.birthDate,
            role: formData.role,
            password: formData.password,
          },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        if (response.status === 200) {
          alert("User added successfully");
          setFormData({
            first_name: "",
            last_name: "",
            email: "",
            birthDate: "",
            role: "",
            password: "",
            confirmPassword: "",
          }); // Reset form after successful submission
        }
      } catch (error) {
        console.error("Error registering user:", error);
        if (error.response && error.response.status === 400) {
          setErrors({ email: error.response.data });
        } else {
          setErrors({
            email: "An unexpected error occurred. Please try again later.",
          });
        }
      } finally {
        setIsSubmitting(false); // Reset submitting state
      }
    }
  };

  return (
    <div className="registration-form">
      <h2 className="form-title">Sign up</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>First Name:</label>
          <input
            type="text"
            name="first_name"
            value={formData.first_name}
            onChange={handleChange}
          />
          {errors.first_name && <p className="error">{errors.first_name}</p>}
        </div>

        <div>
          <label>Last Name:</label>
          <input
            type="text"
            name="last_name"
            value={formData.last_name}
            onChange={handleChange}
          />
          {errors.last_name && <p className="error">{errors.last_name}</p>}
        </div>

        <div>
          <label>Email:</label>
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
          />
          {errors.email && <p className="error">{errors.email}</p>}
        </div>

        <div>
          <label>Birth Date:</label>
          <input
            type="date"
            name="birthDate"
            value={formData.birthDate}
            onChange={handleChange}
          />
          {errors.birthDate && <p className="error">{errors.birthDate}</p>}
        </div>

        <div>
          <label>Position:</label>
          <select
            name="role"
            value={formData.role}
            onChange={handleChange}
          >
            <option value="">Select your post</option>
            <option value="EMPLOYEE">Employee</option>
            <option value="CONTROLLER">Controller</option>
          </select>
          {errors.role && <p className="error">{errors.role}</p>}
        </div>

        <div>
          <label>Password:</label>
          <input
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
          />
          {errors.password && <p className="error">{errors.password}</p>}
          {passwordStrength && (
            <p className={`password-strength ${passwordStrength.toLowerCase()}`}>
              Password Strength: {passwordStrength}
            </p>
          )}
        </div>

        <div>
          <label>Confirm Password:</label>
          <input
            type="password"
            name="confirmPassword"
            value={formData.confirmPassword}
            onChange={handleChange}
          />
          {errors.confirmPassword && <p className="error">{errors.confirmPassword}</p>}
        </div>

        <button type="submit" disabled={isSubmitting}>
          {isSubmitting ? "Registering..." : "Register"}
        </button>
      </form>
    </div>
  );
}

export default AddForm;
