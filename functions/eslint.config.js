import js from "@eslint/js";
import eslintConfigPrettier from "eslint-config-prettier";

export default [
  js.configs.recommended, // Agrega reglas recomendadas
  eslintConfigPrettier // Desactiva reglas de ESLint que puedan entrar en conflicto con Prettier
];

