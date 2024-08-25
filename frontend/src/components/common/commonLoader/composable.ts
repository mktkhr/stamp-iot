export const LOADER_TYPE = {
  FLOWER: {
    id: 1,
    value: 'flower',
  },
  THREE_DOTS: {
    id: 2,
    value: 'three-dots',
  },
  CIRCLE: {
    id: 3,
    value: 'circle',
  },
};

export type LoaderType = (typeof LOADER_TYPE)[keyof typeof LOADER_TYPE];

export const getLoaderTypeFromId = (id: number): LoaderType => {
  return Object.values(LOADER_TYPE).find((item) => item.id === id) ?? LOADER_TYPE.FLOWER;
};

export const useCommonLoader = () => {};
