module.exports = {
  moduleFileExtensions: ['js', 'ts', 'json', 'vue'],
  moduleNameMapper: {
    '^@/(.*)$': '<rootDir>/src/$1',
  },
  transform: {
    '^.+\\.(ts|js)$': 'babel-jest',
    '^.+\\.vue$': '@vue/vue3-jest',
  },
  testEnvironment: 'jest-environment-jsdom',
  //vue3 かつ jest@28以上 の場合に必要
  testEnvironmentOptions: {
    customExportConditions: ['node', 'node-addons'],
  },
  collectCoverage: true,
  collectCoverageFrom: [
    './src/views/**/*.{js,ts}',
    './src/components/**/*.{js,ts}',
    './src/methods/**/*.{js,ts}',
    './src/store/**/*.{js,ts}',
  ],
  globals: {
    'ts-jest': {
      tsconfig: false,
      useESM: true,
      babelConfig: true,
      plugins: ['babel-plugin-transform-vite-meta-env'],
    },
  },
};
