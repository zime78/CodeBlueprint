#!/usr/bin/env node

/**
 * CodeBlueprint MCP Server
 *
 * GoF 23개 디자인 패턴 및 73개 알고리즘 조회를 위한 MCP 서버
 */

import { Server } from "@modelcontextprotocol/sdk/server/index.js";
import { StdioServerTransport } from "@modelcontextprotocol/sdk/server/stdio.js";
import {
  CallToolRequestSchema,
  ListResourcesRequestSchema,
  ListToolsRequestSchema,
  ReadResourceRequestSchema,
} from "@modelcontextprotocol/sdk/types.js";
import { readFileSync } from "fs";
import { fileURLToPath } from "url";
import { dirname, join } from "path";

// 현재 파일 경로 계산
const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

// 패턴 데이터 로드
interface Pattern {
  id: string;
  name: string;
  koreanName: string;
  category: string;
  purpose: string;
  characteristics: string[];
  advantages: string[];
  disadvantages: string[];
  useCases: string[];
  difficulty: string;
  frequency: number;
  relatedPatterns: string[];
  codeExamples: Record<string, string>;
  diagram: string;
}

interface PatternData {
  patterns: Pattern[];
  categories: Record<
    string,
    {
      name: string;
      description: string;
      patterns: string[];
    }
  >;
}

// 알고리즘 데이터 인터페이스
interface Algorithm {
  id: string;
  name: string;
  koreanName: string;
  category: string;
  purpose: string;
  timeComplexity: {
    best: string;
    average: string;
    worst: string;
  };
  spaceComplexity: string;
  characteristics: string[];
  advantages: string[];
  disadvantages: string[];
  useCases: string[];
  difficulty: string;
  frequency: number;
  relatedAlgorithms: string[];
  codeExamples?: Record<string, string>;
}

interface AlgorithmData {
  algorithms: Algorithm[];
  categories: Record<
    string,
    {
      name: string;
      description: string;
      algorithms: string[];
    }
  >;
}

// 데이터 파일 로드
const dataPath = join(__dirname, "..", "data", "patterns.json");
const patternData: PatternData = JSON.parse(readFileSync(dataPath, "utf-8"));

const algorithmDataPath = join(__dirname, "..", "data", "algorithms.json");
const algorithmData: AlgorithmData = JSON.parse(readFileSync(algorithmDataPath, "utf-8"));

/**
 * MCP 서버 인스턴스 생성
 */
const server = new Server(
  {
    name: "codeblueprint",
    version: "1.0.0",
  },
  {
    capabilities: {
      resources: {},
      tools: {},
    },
  }
);

/**
 * 리소스 목록 핸들러
 */
server.setRequestHandler(ListResourcesRequestSchema, async () => {
  const resources = [
    // 패턴 리소스
    {
      uri: "patterns://list",
      name: "전체 패턴 목록",
      description: "GoF 23개 디자인 패턴 전체 목록",
      mimeType: "application/json",
    },
    ...patternData.patterns.map((pattern) => ({
      uri: `patterns://${pattern.id}`,
      name: `${pattern.name} (${pattern.koreanName})`,
      description: pattern.purpose,
      mimeType: "application/json",
    })),
    // 알고리즘 리소스
    {
      uri: "algorithms://list",
      name: "전체 알고리즘 목록",
      description: "73개 알고리즘 전체 목록",
      mimeType: "application/json",
    },
    ...algorithmData.algorithms.map((algorithm) => ({
      uri: `algorithms://${algorithm.id}`,
      name: `${algorithm.name} (${algorithm.koreanName})`,
      description: algorithm.purpose,
      mimeType: "application/json",
    })),
  ];

  return { resources };
});

/**
 * 리소스 읽기 핸들러
 */
server.setRequestHandler(ReadResourceRequestSchema, async (request) => {
  const uri = request.params.uri;

  if (uri === "patterns://list") {
    const list = patternData.patterns.map((p) => ({
      id: p.id,
      name: p.name,
      koreanName: p.koreanName,
      category: p.category,
      purpose: p.purpose,
      difficulty: p.difficulty,
      frequency: p.frequency,
    }));

    return {
      contents: [
        {
          uri,
          mimeType: "application/json",
          text: JSON.stringify(list, null, 2),
        },
      ],
    };
  }

  // patterns://{id} 형식 처리
  const patternMatch = uri.match(/^patterns:\/\/(.+)$/);
  if (patternMatch) {
    const patternId = patternMatch[1];
    const pattern = patternData.patterns.find((p) => p.id === patternId);

    if (pattern) {
      return {
        contents: [
          {
            uri,
            mimeType: "application/json",
            text: JSON.stringify(pattern, null, 2),
          },
        ],
      };
    }
  }

  // 알고리즘 목록
  if (uri === "algorithms://list") {
    const list = algorithmData.algorithms.map((a) => ({
      id: a.id,
      name: a.name,
      koreanName: a.koreanName,
      category: a.category,
      purpose: a.purpose,
      difficulty: a.difficulty,
      frequency: a.frequency,
      timeComplexity: a.timeComplexity.average,
    }));

    return {
      contents: [
        {
          uri,
          mimeType: "application/json",
          text: JSON.stringify(list, null, 2),
        },
      ],
    };
  }

  // algorithms://{id} 형식 처리
  const algorithmMatch = uri.match(/^algorithms:\/\/(.+)$/);
  if (algorithmMatch) {
    const algorithmId = algorithmMatch[1];
    const algorithm = algorithmData.algorithms.find((a) => a.id === algorithmId);

    if (algorithm) {
      return {
        contents: [
          {
            uri,
            mimeType: "application/json",
            text: JSON.stringify(algorithm, null, 2),
          },
        ],
      };
    }
  }

  throw new Error(`리소스를 찾을 수 없습니다: ${uri}`);
});

/**
 * 도구 목록 핸들러
 */
server.setRequestHandler(ListToolsRequestSchema, async () => {
  return {
    tools: [
      {
        name: "list_patterns",
        description:
          "GoF 디자인 패턴 목록을 조회합니다. 카테고리별 필터링 가능.",
        inputSchema: {
          type: "object",
          properties: {
            category: {
              type: "string",
              enum: ["creational", "structural", "behavioral"],
              description:
                "패턴 카테고리 (creational: 생성, structural: 구조, behavioral: 행위)",
            },
          },
        },
      },
      {
        name: "get_pattern",
        description: "특정 패턴의 상세 정보를 조회합니다.",
        inputSchema: {
          type: "object",
          properties: {
            id: {
              type: "string",
              description:
                "패턴 ID (예: singleton, factory_method, observer 등)",
            },
          },
          required: ["id"],
        },
      },
      {
        name: "get_code",
        description: "특정 패턴의 코드 예시를 언어별로 조회합니다.",
        inputSchema: {
          type: "object",
          properties: {
            id: {
              type: "string",
              description: "패턴 ID",
            },
            language: {
              type: "string",
              enum: ["kotlin", "java", "swift", "python"],
              description: "프로그래밍 언어",
            },
          },
          required: ["id", "language"],
        },
      },
      {
        name: "search_patterns",
        description:
          "키워드로 패턴을 검색합니다. 이름, 목적, 활용 예시에서 검색.",
        inputSchema: {
          type: "object",
          properties: {
            keyword: {
              type: "string",
              description: "검색 키워드",
            },
          },
          required: ["keyword"],
        },
      },
      {
        name: "recommend_pattern",
        description:
          "상황 설명을 기반으로 적합한 디자인 패턴을 추천합니다.",
        inputSchema: {
          type: "object",
          properties: {
            description: {
              type: "string",
              description: "해결하려는 문제 또는 상황 설명",
            },
          },
          required: ["description"],
        },
      },
      // 알고리즘 도구
      {
        name: "list_algorithms",
        description:
          "알고리즘 목록을 조회합니다. 카테고리별 필터링 가능.",
        inputSchema: {
          type: "object",
          properties: {
            category: {
              type: "string",
              enum: ["sorting", "searching", "graph", "dynamic_programming", "divide_conquer", "greedy", "backtracking", "string", "math"],
              description:
                "알고리즘 카테고리 (sorting: 정렬, searching: 탐색, graph: 그래프, dynamic_programming: 동적 프로그래밍, divide_conquer: 분할 정복, greedy: 탐욕, backtracking: 백트래킹, string: 문자열, math: 수학)",
            },
          },
        },
      },
      {
        name: "get_algorithm",
        description: "특정 알고리즘의 상세 정보를 조회합니다.",
        inputSchema: {
          type: "object",
          properties: {
            id: {
              type: "string",
              description:
                "알고리즘 ID (예: quick-sort, dijkstra, binary-search 등)",
            },
          },
          required: ["id"],
        },
      },
      {
        name: "search_algorithms",
        description:
          "키워드로 알고리즘을 검색합니다. 이름, 목적, 활용 예시에서 검색.",
        inputSchema: {
          type: "object",
          properties: {
            keyword: {
              type: "string",
              description: "검색 키워드",
            },
          },
          required: ["keyword"],
        },
      },
      {
        name: "recommend_algorithm",
        description:
          "문제 설명을 기반으로 적합한 알고리즘을 추천합니다.",
        inputSchema: {
          type: "object",
          properties: {
            problem: {
              type: "string",
              description: "해결하려는 문제 설명",
            },
          },
          required: ["problem"],
        },
      },
    ],
  };
});

/**
 * 도구 실행 핸들러
 */
server.setRequestHandler(CallToolRequestSchema, async (request) => {
  const { name, arguments: args } = request.params;

  switch (name) {
    case "list_patterns": {
      const category = args?.category as string | undefined;

      let patterns = patternData.patterns;
      if (category) {
        patterns = patterns.filter((p) => p.category === category);
      }

      const categoryInfo = category
        ? patternData.categories[category]
        : null;

      const result = {
        category: categoryInfo
          ? { id: category, ...categoryInfo }
          : { id: "all", name: "전체", description: "모든 GoF 패턴" },
        count: patterns.length,
        patterns: patterns.map((p) => ({
          id: p.id,
          name: p.name,
          koreanName: p.koreanName,
          category: p.category,
          purpose: p.purpose,
          difficulty: p.difficulty,
          frequency: p.frequency,
        })),
      };

      return {
        content: [
          {
            type: "text",
            text: JSON.stringify(result, null, 2),
          },
        ],
      };
    }

    case "get_pattern": {
      const id = args?.id as string;
      const pattern = patternData.patterns.find((p) => p.id === id);

      if (!pattern) {
        return {
          content: [
            {
              type: "text",
              text: `패턴을 찾을 수 없습니다: ${id}\n\n사용 가능한 패턴 ID: ${patternData.patterns.map((p) => p.id).join(", ")}`,
            },
          ],
          isError: true,
        };
      }

      // 관련 패턴 정보 추가
      const relatedPatternDetails = pattern.relatedPatterns.map((relId) => {
        const rel = patternData.patterns.find((p) => p.id === relId);
        return rel
          ? { id: rel.id, name: rel.name, koreanName: rel.koreanName }
          : { id: relId, name: relId, koreanName: "" };
      });

      const result = {
        ...pattern,
        relatedPatternDetails,
        categoryInfo: patternData.categories[pattern.category],
      };

      return {
        content: [
          {
            type: "text",
            text: JSON.stringify(result, null, 2),
          },
        ],
      };
    }

    case "get_code": {
      const id = args?.id as string;
      const language = args?.language as string;

      const pattern = patternData.patterns.find((p) => p.id === id);
      if (!pattern) {
        return {
          content: [
            {
              type: "text",
              text: `패턴을 찾을 수 없습니다: ${id}`,
            },
          ],
          isError: true,
        };
      }

      const code = pattern.codeExamples[language];
      if (!code) {
        const availableLanguages = Object.keys(pattern.codeExamples);
        return {
          content: [
            {
              type: "text",
              text: `${pattern.name} 패턴에 ${language} 코드 예시가 없습니다.\n사용 가능한 언어: ${availableLanguages.join(", ")}`,
            },
          ],
          isError: true,
        };
      }

      const result = {
        pattern: {
          id: pattern.id,
          name: pattern.name,
          koreanName: pattern.koreanName,
        },
        language,
        code,
        diagram: pattern.diagram,
      };

      return {
        content: [
          {
            type: "text",
            text: JSON.stringify(result, null, 2),
          },
        ],
      };
    }

    case "search_patterns": {
      const keyword = (args?.keyword as string).toLowerCase();

      const results = patternData.patterns.filter((p) => {
        const searchableText = [
          p.id,
          p.name,
          p.koreanName,
          p.purpose,
          ...p.characteristics,
          ...p.useCases,
        ]
          .join(" ")
          .toLowerCase();

        return searchableText.includes(keyword);
      });

      return {
        content: [
          {
            type: "text",
            text: JSON.stringify(
              {
                keyword,
                count: results.length,
                results: results.map((p) => ({
                  id: p.id,
                  name: p.name,
                  koreanName: p.koreanName,
                  category: p.category,
                  purpose: p.purpose,
                  useCases: p.useCases,
                })),
              },
              null,
              2
            ),
          },
        ],
      };
    }

    case "recommend_pattern": {
      const description = (args?.description as string).toLowerCase();

      // 키워드 기반 패턴 추천 로직
      const recommendations: { pattern: Pattern; score: number; reason: string }[] = [];

      for (const pattern of patternData.patterns) {
        let score = 0;
        const reasons: string[] = [];

        // 패턴 이름 매칭
        if (
          description.includes(pattern.name.toLowerCase()) ||
          description.includes(pattern.koreanName)
        ) {
          score += 50;
          reasons.push("패턴 이름 언급됨");
        }

        // 용도 매칭
        const purposeWords = pattern.purpose.toLowerCase().split(/\s+/);
        for (const word of purposeWords) {
          if (word.length > 2 && description.includes(word)) {
            score += 10;
          }
        }
        if (score > 0 && !reasons.includes("패턴 이름 언급됨")) {
          reasons.push("목적 키워드 매칭");
        }

        // 사용 사례 매칭
        for (const useCase of pattern.useCases) {
          const useCaseWords = useCase.toLowerCase().split(/\s+/);
          for (const word of useCaseWords) {
            if (word.length > 2 && description.includes(word)) {
              score += 15;
              if (!reasons.includes("활용 사례 매칭")) {
                reasons.push("활용 사례 매칭");
              }
            }
          }
        }

        // 특성 매칭
        for (const char of pattern.characteristics) {
          const charWords = char.toLowerCase().split(/\s+/);
          for (const word of charWords) {
            if (word.length > 2 && description.includes(word)) {
              score += 5;
            }
          }
        }

        // 키워드 기반 가중치 추가
        const keywordPatternMap: Record<string, string[]> = {
          "인스턴스 하나": ["singleton"],
          "전역": ["singleton"],
          "객체 생성": ["factory_method", "abstract_factory", "builder", "prototype"],
          "서브클래스": ["factory_method", "template_method"],
          "복제": ["prototype"],
          "조합": ["builder", "composite"],
          "변환": ["adapter"],
          "래핑": ["adapter", "decorator", "proxy"],
          "분리": ["bridge"],
          "트리": ["composite"],
          "기능 추가": ["decorator"],
          "단순화": ["facade"],
          "메모리": ["flyweight"],
          "대리": ["proxy"],
          "지연": ["proxy"],
          "체인": ["chain_of_responsibility"],
          "실행 취소": ["command", "memento"],
          "순회": ["iterator"],
          "중재": ["mediator"],
          "저장": ["memento"],
          "알림": ["observer"],
          "이벤트": ["observer", "chain_of_responsibility"],
          "상태": ["state"],
          "알고리즘": ["strategy", "template_method"],
          "교체": ["strategy"],
          "방문": ["visitor"],
          "문법": ["interpreter"],
          "DSL": ["interpreter"],
        };

        for (const [keyword, patternIds] of Object.entries(keywordPatternMap)) {
          if (description.includes(keyword) && patternIds.includes(pattern.id)) {
            score += 25;
            if (!reasons.includes("키워드 매칭")) {
              reasons.push("키워드 매칭");
            }
          }
        }

        if (score > 0) {
          recommendations.push({
            pattern,
            score,
            reason: reasons.join(", "),
          });
        }
      }

      // 점수순 정렬
      recommendations.sort((a, b) => b.score - a.score);

      // 상위 5개 추천
      const topRecommendations = recommendations.slice(0, 5);

      if (topRecommendations.length === 0) {
        return {
          content: [
            {
              type: "text",
              text: JSON.stringify(
                {
                  message:
                    "입력된 설명에서 적합한 패턴을 찾지 못했습니다. 더 구체적인 상황을 설명해 주세요.",
                  suggestions: [
                    "객체를 하나만 생성하고 싶을 때 → Singleton",
                    "객체 생성 로직을 분리하고 싶을 때 → Factory Method",
                    "기존 클래스를 새 인터페이스에 맞추고 싶을 때 → Adapter",
                    "이벤트 기반 통신이 필요할 때 → Observer",
                    "알고리즘을 런타임에 교체하고 싶을 때 → Strategy",
                  ],
                },
                null,
                2
              ),
            },
          ],
        };
      }

      const result = {
        query: args?.description,
        recommendations: topRecommendations.map((r, index) => ({
          rank: index + 1,
          id: r.pattern.id,
          name: r.pattern.name,
          koreanName: r.pattern.koreanName,
          category: r.pattern.category,
          purpose: r.pattern.purpose,
          matchScore: Math.min(100, r.score),
          matchReason: r.reason,
          difficulty: r.pattern.difficulty,
        })),
      };

      return {
        content: [
          {
            type: "text",
            text: JSON.stringify(result, null, 2),
          },
        ],
      };
    }

    // 알고리즘 도구 핸들러
    case "list_algorithms": {
      const category = args?.category as string | undefined;

      let algorithms = algorithmData.algorithms;
      if (category) {
        algorithms = algorithms.filter((a) => a.category === category);
      }

      const categoryInfo = category
        ? algorithmData.categories[category]
        : null;

      const result = {
        category: categoryInfo
          ? { id: category, ...categoryInfo }
          : { id: "all", name: "전체", description: "모든 알고리즘" },
        count: algorithms.length,
        algorithms: algorithms.map((a) => ({
          id: a.id,
          name: a.name,
          koreanName: a.koreanName,
          category: a.category,
          purpose: a.purpose,
          timeComplexity: a.timeComplexity.average,
          difficulty: a.difficulty,
          frequency: a.frequency,
        })),
      };

      return {
        content: [
          {
            type: "text",
            text: JSON.stringify(result, null, 2),
          },
        ],
      };
    }

    case "get_algorithm": {
      const id = args?.id as string;
      const algorithm = algorithmData.algorithms.find((a) => a.id === id);

      if (!algorithm) {
        return {
          content: [
            {
              type: "text",
              text: `알고리즘을 찾을 수 없습니다: ${id}\n\n사용 가능한 알고리즘 ID: ${algorithmData.algorithms.map((a) => a.id).join(", ")}`,
            },
          ],
          isError: true,
        };
      }

      // 관련 알고리즘 정보 추가
      const relatedAlgorithmDetails = algorithm.relatedAlgorithms.map((relId) => {
        const rel = algorithmData.algorithms.find((a) => a.id === relId);
        return rel
          ? { id: rel.id, name: rel.name, koreanName: rel.koreanName }
          : { id: relId, name: relId, koreanName: "" };
      });

      const result = {
        ...algorithm,
        relatedAlgorithmDetails,
        categoryInfo: algorithmData.categories[algorithm.category],
      };

      return {
        content: [
          {
            type: "text",
            text: JSON.stringify(result, null, 2),
          },
        ],
      };
    }

    case "search_algorithms": {
      const keyword = (args?.keyword as string).toLowerCase();

      const results = algorithmData.algorithms.filter((a) => {
        const searchableText = [
          a.id,
          a.name,
          a.koreanName,
          a.purpose,
          ...a.characteristics,
          ...a.useCases,
        ]
          .join(" ")
          .toLowerCase();

        return searchableText.includes(keyword);
      });

      return {
        content: [
          {
            type: "text",
            text: JSON.stringify(
              {
                keyword,
                count: results.length,
                results: results.map((a) => ({
                  id: a.id,
                  name: a.name,
                  koreanName: a.koreanName,
                  category: a.category,
                  purpose: a.purpose,
                  timeComplexity: a.timeComplexity.average,
                  useCases: a.useCases,
                })),
              },
              null,
              2
            ),
          },
        ],
      };
    }

    case "recommend_algorithm": {
      const problem = (args?.problem as string).toLowerCase();

      const recommendations: { algorithm: Algorithm; score: number; reason: string }[] = [];

      for (const algorithm of algorithmData.algorithms) {
        let score = 0;
        const reasons: string[] = [];

        // 알고리즘 이름 매칭
        if (
          problem.includes(algorithm.name.toLowerCase()) ||
          problem.includes(algorithm.koreanName)
        ) {
          score += 50;
          reasons.push("알고리즘 이름 언급됨");
        }

        // 용도 매칭
        const purposeWords = algorithm.purpose.toLowerCase().split(/\s+/);
        for (const word of purposeWords) {
          if (word.length > 2 && problem.includes(word)) {
            score += 10;
          }
        }
        if (score > 0 && !reasons.includes("알고리즘 이름 언급됨")) {
          reasons.push("목적 키워드 매칭");
        }

        // 사용 사례 매칭
        for (const useCase of algorithm.useCases) {
          const useCaseWords = useCase.toLowerCase().split(/\s+/);
          for (const word of useCaseWords) {
            if (word.length > 2 && problem.includes(word)) {
              score += 15;
              if (!reasons.includes("활용 사례 매칭")) {
                reasons.push("활용 사례 매칭");
              }
            }
          }
        }

        // 키워드 기반 가중치
        const keywordAlgorithmMap: Record<string, string[]> = {
          "정렬": ["quick-sort", "merge-sort", "heap-sort", "tim-sort"],
          "탐색": ["binary-search", "linear-search", "bfs", "dfs"],
          "최단 경로": ["dijkstra", "bellman-ford", "floyd-warshall", "a-star"],
          "그래프": ["bfs", "dfs", "dijkstra", "prim", "kruskal"],
          "동적 프로그래밍": ["fibonacci-dp", "lcs", "knapsack", "edit-distance"],
          "DP": ["fibonacci-dp", "lcs", "knapsack", "edit-distance"],
          "문자열 검색": ["kmp", "rabin-karp", "boyer-moore"],
          "문자열 매칭": ["kmp", "rabin-karp", "boyer-moore"],
          "최소 신장 트리": ["prim", "kruskal"],
          "MST": ["prim", "kruskal"],
          "소수": ["sieve-eratosthenes", "miller-rabin"],
          "백트래킹": ["n-queens", "sudoku-solver", "subset-sum"],
          "배낭": ["knapsack", "fractional-knapsack"],
        };

        for (const [keyword, algorithmIds] of Object.entries(keywordAlgorithmMap)) {
          if (problem.includes(keyword.toLowerCase()) && algorithmIds.includes(algorithm.id)) {
            score += 25;
            if (!reasons.includes("키워드 매칭")) {
              reasons.push("키워드 매칭");
            }
          }
        }

        if (score > 0) {
          recommendations.push({
            algorithm,
            score,
            reason: reasons.join(", "),
          });
        }
      }

      // 점수순 정렬
      recommendations.sort((a, b) => b.score - a.score);

      // 상위 5개 추천
      const topRecommendations = recommendations.slice(0, 5);

      if (topRecommendations.length === 0) {
        return {
          content: [
            {
              type: "text",
              text: JSON.stringify(
                {
                  message:
                    "입력된 문제에서 적합한 알고리즘을 찾지 못했습니다. 더 구체적인 문제를 설명해 주세요.",
                  suggestions: [
                    "배열을 정렬하고 싶을 때 → Quick Sort, Merge Sort",
                    "정렬된 배열에서 검색할 때 → Binary Search",
                    "최단 경로를 찾을 때 → Dijkstra, A*",
                    "문자열 패턴을 찾을 때 → KMP, Rabin-Karp",
                    "최적화 문제를 풀 때 → Dynamic Programming",
                  ],
                },
                null,
                2
              ),
            },
          ],
        };
      }

      const result = {
        query: args?.problem,
        recommendations: topRecommendations.map((r, index) => ({
          rank: index + 1,
          id: r.algorithm.id,
          name: r.algorithm.name,
          koreanName: r.algorithm.koreanName,
          category: r.algorithm.category,
          purpose: r.algorithm.purpose,
          timeComplexity: r.algorithm.timeComplexity,
          matchScore: Math.min(100, r.score),
          matchReason: r.reason,
          difficulty: r.algorithm.difficulty,
        })),
      };

      return {
        content: [
          {
            type: "text",
            text: JSON.stringify(result, null, 2),
          },
        ],
      };
    }

    default:
      return {
        content: [
          {
            type: "text",
            text: `알 수 없는 도구입니다: ${name}`,
          },
        ],
        isError: true,
      };
  }
});

/**
 * 서버 시작
 */
async function main() {
  const transport = new StdioServerTransport();
  await server.connect(transport);
  console.error("CodeBlueprint MCP 서버가 시작되었습니다.");
}

main().catch(console.error);
