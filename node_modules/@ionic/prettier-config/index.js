module.exports = {
  arrowParens: 'avoid',
  bracketSpacing: true,
  jsxBracketSameLine: false,
  jsxSingleQuote: false,
  quoteProps: 'consistent',
  semi: true,
  singleQuote: true,
  tabWidth: 2,
  trailingComma: 'all',
  overrides: [
    {
      files: ['*.java'],
      options: {
        printWidth: 140,
        tabWidth: 4,
        useTabs: false,
        trailingComma: 'none',
      },
    },
  ],
};
